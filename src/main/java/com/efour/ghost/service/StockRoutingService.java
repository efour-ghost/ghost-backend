package com.efour.ghost.service;

import com.efour.ghost.ui.dto.TickerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockRoutingService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendStock(TickerDto ticker){
        simpMessagingTemplate.convertAndSend("/getStock", ticker);
    }
}
