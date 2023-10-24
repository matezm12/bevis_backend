package com.bevis.certbuilder;

import com.bevis.certbuilder.dto.AssetCertificateContext;

import java.io.IOException;
import java.io.OutputStream;

public interface AssetCertificateBuilder {
    void constructCertificate(AssetCertificateContext context, OutputStream outputStream) throws IOException;
}
