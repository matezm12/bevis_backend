package com.bevis.appbe.web.rest.admin;

import com.bevis.files.dto.File;
import com.bevis.ipfs.IpfsService;
import com.bevis.ipfs.dto.IpfsFile;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.bevis.files.util.FileUtil.createTempFile;
import static com.bevis.security.AuthoritiesConstants.ADMIN;

@Slf4j
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class IpfsController {

    private final IpfsService ipfsService;

    @Secured(ADMIN)
    @PostMapping("/admin/ipfs/upload")
    IpfsFile fileUpload(MultipartFile file) {
        File tmpFile = createTempFile(file);
        return ipfsService.postFileAndPin(tmpFile);
    }

    @Secured(ADMIN)
    @PostMapping("/admin/ipfs/pin/{cid}")
    boolean fileUpload(@PathVariable String cid) {
        return ipfsService.pinFile(cid);
    }
}
