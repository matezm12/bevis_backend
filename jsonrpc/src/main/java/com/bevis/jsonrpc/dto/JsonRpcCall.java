package com.bevis.jsonrpc.dto;

import lombok.Data;

import java.util.HashMap;

@Data
public class JsonRpcCall {
    private String id;
    private String method;
    private Object params = new HashMap<>();

}
