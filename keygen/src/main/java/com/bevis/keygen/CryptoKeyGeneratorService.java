package com.bevis.keygen;

import java.util.List;

public interface CryptoKeyGeneratorService {
    List<CryptoKeyDTO> generate(String currency, Integer count);
    CryptoKeyDTO generate(String currency);
}
