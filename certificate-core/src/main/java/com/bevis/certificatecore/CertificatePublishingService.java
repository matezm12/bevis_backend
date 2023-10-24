package com.bevis.certificatecore;

import com.bevis.certificatecore.dto.CertPublishDTO;

public interface CertificatePublishingService {
    void publish(CertPublishDTO certPublishDTO) throws Exception;
}
