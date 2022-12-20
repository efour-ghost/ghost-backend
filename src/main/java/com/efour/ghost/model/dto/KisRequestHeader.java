package com.efour.ghost.model.dto;

import lombok.Builder;

@Builder
public class KisRequestHeader {
    private String appkey;
    private String appsecret;
    private String custType;
    private String contentType;
    private String trId;
}
