package com.bevis.common.util;

import io.github.pixee.security.Newlines;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FileDownloader {

    private Logger LOGGER = LoggerFactory.getLogger(FileDownloader.class);

    private Map<String, String> headers = new HashMap<>();

    private String fileName = "default_filename";
    private Integer contentLength = 0;
    private String contentType = "application/x-download";

    public FileDownloader setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public FileDownloader setContentLength(Integer contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    public FileDownloader addHeader(String key, String value){
        headers.put(key, value);
        return this;
    }

    public FileDownloader setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public void download(InputStream inputStream, HttpServletResponse response) throws IOException {
        response.setContentType(contentType);
        response.setHeader("Content-Disposition", Newlines.stripAll("inline; filename=" + fileName));
        headers.forEach(response::addHeader);
        response.setContentLength(contentLength);
        IOUtils.copy(inputStream, response.getOutputStream());
        response.flushBuffer();
        LOGGER.info(String.format("File %s sent successfully", fileName));
    }

    public static void downloadFile(java.io.File file, HttpServletResponse response) throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            new FileDownloader()
                    .setFileName(file.getName())
                    .setContentType(java.nio.file.Files.probeContentType(file.toPath()))
                    .setContentLength((int) file.length())
                    .download(inputStream, response);
        }
    }




}
