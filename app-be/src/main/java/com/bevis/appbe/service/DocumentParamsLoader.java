package com.bevis.appbe.service;

import com.bevis.bevisassetpush.dto.FileParametersDTO;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentParamsLoader {

    FileParametersDTO getFileParametersFromDocument(MultipartFile file);
}
