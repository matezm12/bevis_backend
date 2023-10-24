package com.bevis.assetimport.codereadr;

import com.bevis.assetimport.AssetImportApplication;
import com.bevis.assetimport.codereadr.CodeReadrImportMapper;
import com.bevis.assetimport.dto.AssetImportDTO;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AssetImportApplication.class)
@Ignore
public class CodeReadrImportMapperTest  {

    @Test
    public void importFromCodereader() {
        String request = "User+Name=service%40reardenmetals.sg&Scan+ID=113942531&Device+ID=291030&Barcode=qzzt5gd9kfeuux49e3x3u2er6p4vujc5nvcafmrv8g%0Aqrtfh9pcj5fwqg2h52pftxnxecvj455nzqupprq250%0Aqqdkez4dt79xfwlmu5enrz6rjz0ulxjs0ckajug63n%0Aqqkms06z8fpv2yxvsh0a59tefehva9fulv2dx63xn0%0Aqq5mxvrytual6evd0jdffvmdqqlutg0huytw0x3xtf%0Aqr9uphrsmpcgfdps8vm0yhmfcqvugvy7uu2hkscttc%0Aqq6xfpx2kycmr4cpg3xe8xg7c8ahej5wsgrlkrh33q%0Aqrmrrcn5suh3maemzd4t4cyeaww74xtlwu0s4jewfu%0Aqr9fu8yf3gtnmg35jfy3vs2nlpgdrjqqnu468pwmxk%0Aqpwvrukj7nn5f0znxc48ywmwwh7n9y3afqvm5tg0tx%0Aqps76th0ed8098tysvmhc2zaqesna365vy7ejt8j0e%0Aqr8368zxmcjalkwvrf02d2tndxh899kfvs66mvvnml%0Aqrecgjycctfyj9dt6n00apmtlcwd7sc7pga9m44gvj%0A601285372563&Timestamp+%28Scanned%29=2020-01-08+12%3A58%3A21&Timestamp+%28Received%29=2020-01-08+12%3A58%3A37&Property%3A+gps_location=geo%3A%2F%2F13.7323119%2C100.5642468%2C0.0%2C500.0%2C66%2Cnetwork%2Caddress%7B15+Sukhumvit+20+Alley%2C+Khwaeng+Khlong+Toei%2C+Khet+Khlong+Toei%2C+Krung+Thep+Maha+Nakhon+10110%2C+Thailand%7D";
        CodeReadrImportMapper codeReadrImportMapper = new CodeReadrImportMapper();
        final AssetImportDTO assetImportDTO = codeReadrImportMapper.mapFromCodeReadrRequest(request);
        assertNotNull("Not null?", assetImportDTO);
    }

    @Test
    public void importFromReardenCodereader() {
        String request = "Question%3A+productUpc=764283494429&Question%3A+cartonAssetId=qp9p0s9ggmqdnrud2cdwu2z6q9tlu6j7lvyuy9sfdt&User+Name=service%40reardenmetals.sg&Scan+ID=113942531&Device+ID=291030&Barcode=qzzt5gd9kfeuux49e3x3u2er6p4vujc5nvcafmrv8g%0Aqrtfh9pcj5fwqg2h52pftxnxecvj455nzqupprq250%0Aqqdkez4dt79xfwlmu5enrz6rjz0ulxjs0ckajug63n%0Aqqkms06z8fpv2yxvsh0a59tefehva9fulv2dx63xn0%0Aqq5mxvrytual6evd0jdffvmdqqlutg0huytw0x3xtf%0Aqr9uphrsmpcgfdps8vm0yhmfcqvugvy7uu2hkscttc%0Aqq6xfpx2kycmr4cpg3xe8xg7c8ahej5wsgrlkrh33q%0Aqrmrrcn5suh3maemzd4t4cyeaww74xtlwu0s4jewfu%0Aqr9fu8yf3gtnmg35jfy3vs2nlpgdrjqqnu468pwmxk%0Aqpwvrukj7nn5f0znxc48ywmwwh7n9y3afqvm5tg0tx%0Aqps76th0ed8098tysvmhc2zaqesna365vy7ejt8j0e%0Aqr8368zxmcjalkwvrf02d2tndxh899kfvs66mvvnml%0Aqrecgjycctfyj9dt6n00apmtlcwd7sc7pga9m44gvj%0A601285372563&Timestamp+%28Scanned%29=2020-01-08+12%3A58%3A21&Timestamp+%28Received%29=2020-01-08+12%3A58%3A37&Property%3A+gps_location=geo%3A%2F%2F13.7323119%2C100.5642468%2C0.0%2C500.0%2C66%2Cnetwork%2Caddress%7B15+Sukhumvit+20+Alley%2C+Khwaeng+Khlong+Toei%2C+Khet+Khlong+Toei%2C+Krung+Thep+Maha+Nakhon+10110%2C+Thailand%7D";
        CodeReadrImportMapper codeReadrImportMapper = new CodeReadrImportMapper();
        final AssetImportDTO assetImportDTO = codeReadrImportMapper.mapFromCodeReadrRequest(request);
        assertNotNull("Not null?", assetImportDTO);
    }

    @Test
    public void importFromReardenCodereader2() {
        String request = "Service+ID=1581024&Question%3A+productUpc=&Question%3A+cartonAssetId=&Question%3A+orderNumber=%2320-1246&Question%3A+shipmentAssetId=qp9pf7z4drnj6vn6rfc8nsjpq3ckqjpx2suxhhdqjk&Question%3A+carrier=&Question%3A+trackingNumber=&User+Name=service%40reardenmetals.sg&Scan+ID=127250920&Device+ID=330111&Barcode=qp9pez47jjc2srkk7haw8866gyxjd903pg0yv9xcqz%0Aqp9pehhhddxkn3u5zn4tl6lfkxvrtp8kxuvyrye0t8&Timestamp+%28Scanned%29=2020-07-01+23%3A14%3A18&Timestamp+%28Received%29=2020-07-02+05%3A43%3A31&Property%3A+gps_location=geo%3A%2F%2F1.3815204%2C103.9694251%2C38.39999771118164%2C15.646%2C17%2Cnetwork%2Caddress%7B25B+Loyang+Cres%2C+Singapore%7D";
        CodeReadrImportMapper codeReadrImportMapper = new CodeReadrImportMapper();
        final AssetImportDTO assetImportDTO = codeReadrImportMapper.mapFromCodeReadrRequest(request);
        assertNotNull("Not null?", assetImportDTO);
    }
}
