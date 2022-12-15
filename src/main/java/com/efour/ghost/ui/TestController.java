package com.efour.ghost.ui;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class TestController {
    @GetMapping("/home")
    public String test(){
        return LocalDateTime.now().toString();
    }

}
