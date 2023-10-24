package com.bevis.appbe.web.rest.util;

import com.bevis.bevisassetpush.dto.FileParametersDTO;
import org.springframework.web.multipart.MultipartFile;

import static com.bevis.files.util.FileUtil.getFileExtension;

public final class MultipartFileUtil {

    public static FileParametersDTO getParametersFromMultipartFile(MultipartFile multipartFile, boolean encrypted){
        final long size = multipartFile.getSize();
        final String fileExt = encrypted ? "zip" : getFileExtension(multipartFile.getOriginalFilename()).substring(1);
        return new FileParametersDTO(size, fileExt, multipartFile.getOriginalFilename());
    }

    public static FileParametersDTO getParametersFromMultipartFile(MultipartFile multipartFile){
        return getParametersFromMultipartFile(multipartFile, false);
    }
}
