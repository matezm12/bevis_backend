package com.bevis.certificate;

import java.io.OutputStream;

public interface CertificateService {
    void constructCertificateForAsset(String assetId, OutputStream outputStream) throws Exception;
}
