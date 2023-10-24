package com.bevis.keygen;

import lombok.Data;

@Data
class KeygenRequest {
    private String blockchain;
    private Long quantity;
}
