package com.shangchain.straitchain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 2022/4/26
 * θΏεη»ζ
 *
 * @author shangchain Junisyoan
 */
@Data
public class StraitChainResponse {
    public Integer id;
    public Object result;
    public Error error;
    private String jsonrpc;

    @NoArgsConstructor
    @Data
    public static class Error {
        public Integer code;
        public String message;
    }
}

