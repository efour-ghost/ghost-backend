package com.efour.ghost.ui.dto;

import lombok.Builder;

@Builder
public class TickerDto {
    private String code;
    private Integer currentPrice; // 주식 현재가
    private Integer dayOnDayPrice; // 전일 대비 가격 비교
    private Double dayOnDayPriceRate; // 전일 대비율

    public String toString(){
        return "[종목 코드: " + code + ", 주식 현재가: " + currentPrice +", 전일 대비 가격 비교: " + dayOnDayPrice + ", 전일 대비율: " + dayOnDayPriceRate+"]";
    }
}
