package com.efour.ghost.util;

import com.efour.ghost.config.properties.KISProperty;
import com.efour.ghost.model.dto.KisRequestHeader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WebsocketClient {
    private WebsocketClientEndpoint clientEndPoint;
    @Autowired
    private KISProperty kisProperty;

    @Value("${kis.url}")
    private String kisUrl;

    @Value("${kis.port}")
    private String kisPort;
    private String stockCode = "000660";
    private String trType = "1";

    public WebsocketClient() {
        try {
            clientEndPoint = new WebsocketClientEndpoint(new URI("ws://ops.koreainvestment.com:21000"));
            clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
                public void handleMessage(String message) {
                    System.out.println(message);
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(){
//        KisRequestHeader requestHeader = KisRequestHeader.builder()
//                .appkey(kisProperty.getAppkey())
//                .appsecret(kisProperty.getAppsecret())
//                .custType(kisProperty.getCustType())
//                .contentType(kisProperty.getContentType())
//                .build();
        String[][] code_list =
                {
                        {"1", "H0STASP0", "005930"},
                        {"1", "H0STASP0", "009540"},
                        {"1", "H0STASP0", "035720"},
                        {"1", "H0STASP0", "139260"},
                        {"1", "H0STASP0", "132030"},
                        {"1", "H0STASP0", "28513K"},
                        {"1", "H0STASP0", "207940"},
                        {"1", "H0STASP0", "051910"},
                        {"1", "H0STASP0", "000100"},
                        {"1", "H0STASP0", "233160"},
                        {"1", "H0STASP0", "120115"},
                        {"1", "H0STASP0", "102110"},
                        {"1", "H0STASP0", "035420"},
                        {"1", "H0STASP0", "011790"},
                        {"1", "H0STASP0", "005390"},
                        {"1", "H0STASP0", "00499K"},
                        {"1", "H0STASP0", "000270"},
                        {"1", "H0STASP0", "377300"},
                        {"1", "H0STASP0", "375500"},
                        {"1", "H0STASP0", "26490K"}
                };
        for(int i=0;i<20;i++){
            Map<String,Object> header = new HashMap<String,Object>();
            header.put("appkey", kisProperty.getAppkey());
            header.put("appsecret", kisProperty.getAppsecret());
            header.put("custtype", kisProperty.getCustType());
            header.put("content-type", kisProperty.getContentType());
            header.put("tr_type", trType);

            Map<String, Object> body = new HashMap<String, Object>();
            body.put("tr_id", kisProperty.getTrId());
            body.put("tr_key", code_list[i][2]);

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("header", header);

            Map<String, Object> data2 = new HashMap<String, Object>();
            data2.put("input", body);
            data.put("body", data2);

            try {
                String sendJson = new ObjectMapper().writeValueAsString(data);
                clientEndPoint.sendMessage(sendJson);
            }catch (JsonProcessingException e){

            }
        }


    }

}
