package com.efour.ghost.ui;

import com.efour.ghost.ui.dto.TickerDto;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@AllArgsConstructor
@Controller
public class StockSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;


    @Scheduled(fixedRate = 5000)
    @SendTo("/topic/stocks")
    public void send() throws Exception {
        simpMessagingTemplate.convertAndSend("/topic/stocks",
                TickerDto.builder()
                        .code("0123")
                        .currentPrice(50000)
                        .dayOnDayPrice(3000)
                        .dayOnDayPriceRate(0.1)
                        .build()
                        .toString());
    }
}
