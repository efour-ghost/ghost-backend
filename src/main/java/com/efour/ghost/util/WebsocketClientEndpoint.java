package com.efour.ghost.util;

import jakarta.websocket.*;
import org.apache.tomcat.util.json.ParseException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URI;
import java.util.Arrays;

@ClientEndpoint
public class WebsocketClientEndpoint {
    private Session userSession = null;
    private MessageHandler messageHandler;

    public WebsocketClientEndpoint(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }

    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println(">> Connected websocket. Ready!!");
        this.userSession = userSession;
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("closing websocket");
        this.userSession = null;
    }

    @OnMessage
    public void onMessage(String message) {
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage("[RECV] :"+message);
            char fStr = message.charAt(0);

            // 첫데이터로 전문인지 json 데이터인지 구분을해서 처리를 해야한다.
            if(fStr == '0') {
                // 암호화 되지 않은 전문 처리
                String[] mData = message.split("\\|");
                String tr_id = mData[1];
                if("H0STASP0".equals(tr_id)) {
                    System.out.println("[" + tr_id + "]" );
                    String[] recvvalue = mData[3].split("\\^");
                    System.out.println(Arrays.toString(recvvalue));
                    System.out.println("유가증권 단축 종목코드 [" + recvvalue[0] + "]");
                    System.out.println("영업시간 [" + recvvalue[1] + "]" + "시간구분코드 [" + recvvalue[2] + "]");
                    System.out.printf("총매도호가 잔량        [%s]\r\n",recvvalue[43]);
                    System.out.printf("총매도호가 잔량 증감    [%s]\r\n",recvvalue[54]);
                    System.out.printf("총매수호가 잔량        [%s]\r\n",recvvalue[44]);
                    System.out.printf("총매수호가 잔량 증감    [%s]\r\n",recvvalue[55]);
                    System.out.printf("시간외 총매도호가 잔량  [%s]\r\n",recvvalue[45]);
                    System.out.printf("시간외 총매수호가 증감  [%s]\r\n",recvvalue[46]);
                    System.out.printf("시간외 총매도호가 잔량  [%s]\r\n",recvvalue[56]);
                    System.out.printf("시간외 총매수호가 증감  [%s]\r\n",recvvalue[57]);
                    System.out.printf("예상 체결가           [%s]\r\n",recvvalue[47]);
                    System.out.printf("예상 체결량           [%s]\r\n",recvvalue[48]);
                    System.out.printf("예상 거래량           [%s]\r\n",recvvalue[49]);
                    System.out.printf("예상체결 대비          [%s]\r\n",recvvalue[50]);
                    System.out.printf("부호                 [%s]\r\n",recvvalue[51]);
                    System.out.printf("예상체결 전일대비율     [%s]\r\n",recvvalue[52]);
                    System.out.printf("누적거래량            [%s]\r\n",recvvalue[53]);
                    System.out.printf("주식매매 구분코드       [%s]\r\n",recvvalue[58]);
                }
            }else {
                // 일반 json 처리
                JSONParser parser = new JSONParser();
                Object obj;
                JSONObject jsonObj;
                try {
                    obj = parser.parse(message);
                    jsonObj = (JSONObject) obj;
                    JSONObject header = (JSONObject) jsonObj.get("header");

                    String tmp_key = "";
                    String tmp_iv = "";

                    // tr_id 로 데이터처리를 구분한다.
                    String tr_id = header.get("tr_id").toString();

                    // 일반 요청에 대한 응답 데이터일 경우
                    if (!(tr_id.equals("PINGPONG")))
                    {
                        // 일반 요청에 대한 응답 데이터에만 body 가 있다.
                        JSONObject body = (JSONObject) jsonObj.get("body");
                        String rt_cd = body.get("rt_cd").toString();

                        String rt_msg = body.get("msg_cd").toString();
                        String msg = body.get("msg1").toString();
                        // rt_cd 가 '0'인경우에만 처리한다.
                        if (rt_cd.equals("0"))	{
                            JSONObject output = (JSONObject) body.get("output");
                            //String rt_msg = body.get("msg_cd").toString();
                            tmp_key = output.get("key").toString();
                            tmp_iv = output.get("iv").toString();
                        } else {
                            rt_msg = body.get("msg1").toString();
                        }


                        switch (tr_id)	{
                            case "H0STASP0":	// 주식호가
                                System.out.println("주식호가 ["+rt_msg+"] ["+msg+"]");
                                break;
                            case "H0STCNT0":
                                System.out.println("주식체결 ["+rt_msg+"] ["+msg+"]");
                                break;
                            default:
                                break;
                        }
                    }
                    else if (tr_id.equals("PINGPONG"))	// PINGPONG 데이터일 경우
                    {
                        sendMessage(message);
                        System.out.println("[SEND] :"+ message);
                    }

                } catch (org.json.simple.parser.ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        }
    }

    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }

    public static interface MessageHandler {

        public void handleMessage(String message);
    }
}
