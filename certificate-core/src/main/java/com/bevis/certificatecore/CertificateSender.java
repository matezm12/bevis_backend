package com.bevis.certificatecore;

import java.io.File;

public interface CertificateSender {
    void send(String receiverEmail, Object certificateData, File file);
}
