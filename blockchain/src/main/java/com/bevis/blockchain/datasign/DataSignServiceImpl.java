package com.bevis.blockchain.datasign;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

@Service
@RequiredArgsConstructor
class DataSignServiceImpl implements DataSignService {

    private static final int HASH_LENGTH = 8;

    private final DataSignProps dataSignProps;

    @Override
    public String signTxData(String unsignedData, String address) {
        final String unsignedDataWithNonce = unsignedData + "." + generateNonce();
        String generatedHash = generateSignHash(unsignedDataWithNonce, address);
        String resultHash = concatHash(generatedHash);
        return unsignedDataWithNonce + "." + resultHash;
    }

    @Override
    public boolean validateSignedData(String signedData, String address) {
        int i = signedData.lastIndexOf(".");
        String unsignedData = signedData.substring(0, i);
        String signedHash = signedData.substring(i + 1);
        String generatedHash = generateSignHash(unsignedData, address);
        String calculatedHash = concatHash(generatedHash);
        return Objects.equals(calculatedHash, signedHash);
    }

    private String generateSignHash(String unsignedData, String address) {
        String secret = dataSignProps.getSecret();
        String dataHash = DigestUtils.sha256Hex(unsignedData);
        String addressHash = DigestUtils.sha256Hex(address);
        String secretHash = DigestUtils.sha256Hex(secret);
        return DigestUtils.sha256Hex(dataHash + addressHash + secretHash);
    }

    private String generateNonce() {
        return DigestUtils.sha256Hex(String.valueOf(Instant.now().getEpochSecond())).substring(0, 5);
    }

    private String concatHash(String generatedHash) {
        return generatedHash.substring(0, HASH_LENGTH);
    }
}
