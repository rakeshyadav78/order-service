package com.tgd.order.dto;

import lombok.Data;

@Data
public class ResponseDTO <T>{
    private String errorCode;
    private String message;
    private T data;
}
