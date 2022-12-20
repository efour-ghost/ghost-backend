package com.efour.ghost.ui;

import com.efour.ghost.util.WebsocketClient;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
public class TestController {
    private final WebsocketClient websocketClient;

    @GetMapping("/home")
    public String test(){
        return LocalDateTime.now().toString();
    }

    @GetMapping("/getStock")
    public String getStock(){
        websocketClient.sendMessage();
        return "start";
    }

}
