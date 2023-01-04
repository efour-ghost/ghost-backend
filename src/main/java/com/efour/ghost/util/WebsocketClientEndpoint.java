package com.efour.ghost.util;

import com.efour.ghost.ui.dto.TickerDto;
import jakarta.websocket.*;
import org.apache.tomcat.util.json.ParseException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URI;
import java.util.Date;

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
            char fStr = message.charAt(0);

            if(fStr == '0') {
                // 암호화 되지 않은 전문 처리
                String[] mData = message.split("\\|");
                String tr_id = mData[1];

                if ("H0STCNT0".equals(tr_id)){
                    String[] arrValue = mData[3].split("\\^");

                    for (int i=0; i<arrValue.length;i++)	{
                   //     System.out.printf("%-12s : [%s]\r\n", arrMenu[i], arrValue[i]);
                    }

                    TickerDto ticker  = TickerDto.builder()
                                .code(arrValue[0])
                                .time(arrValue[1])
                                .currentPrice(Integer.parseInt(arrValue[2]))
                                .dayOnDayPriceType(Integer.parseInt(arrValue[3]))
                                .dayOnDayPrice(Integer.parseInt(arrValue[4]))
                                .dayOnDayPriceRate(Double.parseDouble(arrValue[5]))
                                .build();

                            System.out.println(ticker.toString());

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

                    // tr_id 로 데이터처리를 구분한다.
                    String tr_id = header.get("tr_id").toString();

                    if (tr_id.equals("PINGPONG"))	// PINGPONG 데이터일 경우
                    {
                        sendMessage(message);
                       //TODO : logger 추가
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
