package com.bevis.jsonrpc;

import com.bevis.jsonrpc.dto.JsonRpcCall;
import com.bevis.jsonrpc.dto.JsonRpcResponse;
import com.bevis.jsonrpc.dto.JsonRpcSecuredCall;

public interface JsonRpcService {
    <T extends JsonRpcResponse> T processJsonRpcCall(String url, JsonRpcCall rpcCall, Class<T> responseType);
    <T extends JsonRpcResponse> T processJsonRpcCall(String url, JsonRpcSecuredCall rpcCall, Class<T> responseType);
}
