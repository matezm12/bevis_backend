package com.bevis.appbe.service;

import com.bevis.appbe.web.rest.util.MultipartFileUtil;
import com.bevis.bevisassetpush.dto.FileParametersDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentParamsLoaderImpl implements DocumentParamsLoader {

    @Override
    public FileParametersDTO getFileParametersFromDocument(MultipartFile file) {
        return MultipartFileUtil.getParametersFromMultipartFile(file);
    }
}
