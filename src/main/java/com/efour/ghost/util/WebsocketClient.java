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
import java.util.HashMap;
import java.util.Map;

@Component
public class WebsocketClient {
    private WebsocketClientEndpoint clientEndPoint;
    @Autowired
    private KISProperty kisProperty;

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
        String[] code_list = {
                "009540", "035720","139260", "132030", "28513K"
                , "051910", "000100", "233160", "120115", "102110"
                , "035420", "011790", "005390", "00499K", "000270"
                , "377300", "375500", "26490K", "005930", "207940"
        };

        for(int i=0;i<20;i++){
            //TODO : Property config builder
            //        KisRequestHeader requestHeader = KisRequestHeader.builder()
//                .appkey(kisProperty.getAppkey())
//                .appsecret(kisProperty.getAppsecret())
//                .custType(kisProperty.getCustType())
//                .contentType(kisProperty.getContentType())
//                .build();
            Map<String,Object> header = new HashMap<String,Object>();
            header.put("appkey", kisProperty.getAppkey());
            header.put("appsecret", kisProperty.getAppsecret());
            header.put("custtype", kisProperty.getCustType());
            header.put("content-type", kisProperty.getContentType());
            header.put("tr_type", "1");

            Map<String, Object> body = new HashMap<String, Object>();
            body.put("tr_id", kisProperty.getTrId());
            body.put("tr_key", code_list[i]);

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
