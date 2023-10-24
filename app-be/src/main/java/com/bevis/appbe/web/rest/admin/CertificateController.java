package com.bevis.appbe.web.rest.admin;

import com.bevis.certificatecore.CertificatePublishingService;
import com.bevis.certificatecore.dto.CertPublishDTO;
import com.bevis.certificate.CertificateService;
import com.bevis.common.util.FileDownloader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static com.bevis.security.AuthoritiesConstants.ADMIN;
import static com.bevis.security.AuthoritiesConstants.VENDOR;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
class CertificateController {

    private final CertificatePublishingService certificatePublishingService;
    private final CertificateService certificateService;

    @Secured(ADMIN)
    @GetMapping("admin/certificate/preview")
    void loadCertificatePreview(@RequestParam("asset-id") String assetId,
                                HttpServletResponse response) throws Exception {
        log.debug("loadCertificatePreview");
        byte[] bytes;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            certificateService.constructCertificateForAsset(assetId, byteArrayOutputStream);
            bytes = byteArrayOutputStream.toByteArray();
        }
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            new FileDownloader()
                    .setFileName("certificate.html")
                    .setContentType("text/html")
                    .setContentLength(bytes.length)
                    .download(inputStream, response);
        }
    }

    @Secured(ADMIN)
    @PostMapping("admin/certificate/publish")
    void publishCertificate(@RequestBody @Valid CertPublishDTO certPublishDTO) throws Exception {
        log.debug("publishAsset");
        certificatePublishingService.publish(certPublishDTO);
    }
}
