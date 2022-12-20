package com.efour.ghost.util;

import com.efour.ghost.config.properties.KISProperty;
import com.efour.ghost.model.dto.KisRequestHeader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    private String stockCode = "000660";
    private String trType = "1";

    public WebsocketClient() {
        try {
            clientEndPoint = new WebsocketClientEndpoint(new URI(kisProperty.getUrl()+":"+kisProperty.getPort()));
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

        Map<String,Object> header = new HashMap<String,Object>();
        header.put("appkey", kisProperty.getAppkey());
        header.put("appsecret", kisProperty.getAppsecret());
        header.put("custtype", kisProperty.getCustType());
        header.put("content-type", kisProperty.getContentType());
        header.put("tr_type", trType);


        Map<String, Object> body = new HashMap<String, Object>();
        body.put("tr_id", kisProperty.getTrId());
        body.put("tr_key", "033180");

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
