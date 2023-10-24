package com.bevis.jsonrpc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JsonRpcSecurity {
    private String user;
    private String password;
}
