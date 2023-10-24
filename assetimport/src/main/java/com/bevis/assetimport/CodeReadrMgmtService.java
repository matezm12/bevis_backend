package com.bevis.assetimport;

import com.bevis.assetimport.domain.CodeReadrService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CodeReadrMgmtService {
    Page<CodeReadrService> searchAll(String search, Pageable pageable);

    Optional<CodeReadrService> findById(Long id);

    CodeReadrService save(CodeReadrService codeReadrService);

    void deleteById(Long id);
}
