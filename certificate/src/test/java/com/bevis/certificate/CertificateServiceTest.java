package com.bevis.certificate;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CertificateServiceTest extends TestCase {

    @Autowired
    private CertificateService certificateService;

    @Test
    public void testConstructCertificateForAsset() throws Exception {
        String testAssetId = "ptuzng";
        File file = new File("cert2.html");
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            certificateService.constructCertificateForAsset(testAssetId, fileOutputStream);
        }

        log.info("Finish");
    }
}
