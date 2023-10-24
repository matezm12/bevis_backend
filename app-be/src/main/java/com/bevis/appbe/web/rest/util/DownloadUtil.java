package com.bevis.appbe.web.rest.util;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;

public final class DownloadUtil {
    public static ResponseEntity<Resource> getDownloadFileResponse(InputStream inputStream, String fileName, int length) {
        InputStreamResource resource = new InputStreamResource(inputStream);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Suggested-Filename", fileName);
        httpHeaders.add("Content-Disposition", "inline; filename=" + fileName);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentLength(length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
}
