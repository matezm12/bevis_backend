package com.bevis.jsonrpc.dto;

import lombok.Data;

@Data
public class JsonRpcResponse<T> {
    private String id;
    private T result;
    private JsonRpcError error;
}
