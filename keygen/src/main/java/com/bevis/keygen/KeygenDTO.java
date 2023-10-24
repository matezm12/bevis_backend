package com.bevis.keygen;

import lombok.Data;

import java.util.List;

@Data
class KeygenDTO {
    private List<CryptoKeyDTO> data;
    private Metadata metadata;

    @Data
    static class Metadata {
        private String blockchain;
        private Long quantity;
    }
}
