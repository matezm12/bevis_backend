package com.bevis.jsonrpc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JsonRpcSecuredCall {
    private JsonRpcCall jsonRpcCall;
    private JsonRpcSecurity security;

    public static JsonRpcSecuredCall of(JsonRpcCall jsonRpcCall, JsonRpcSecurity security){
        return new JsonRpcSecuredCall(jsonRpcCall, security);
    }

}
