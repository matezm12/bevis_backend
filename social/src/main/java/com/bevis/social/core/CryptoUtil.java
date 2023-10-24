package com.bevis.social.core;

import com.google.gson.Gson;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Base64;

public final class CryptoUtil {
    public static PrivateKey privateKeyFromPem(String privateKey) throws IOException {
        String keyData = base64DecodeString(privateKey);
        final Reader pemReader = new StringReader(keyData);
        final PEMParser pemParser = new PEMParser(pemReader);
        final JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        final PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
        return converter.getPrivateKey(object);
    }

    public static String base64DecodeString(String s) {
        // Base64 decode the string
        byte[] decodedBytes = Base64.getDecoder().decode(s);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    public static <T> T parseJwtTokenBody(String jwtToken, Class<T> clazz) {
        String[] splitToken = jwtToken.split("\\.");
        String jsonBody = base64DecodeString(splitToken[1]);
        return new Gson().fromJson(jsonBody, clazz);
    }
}
