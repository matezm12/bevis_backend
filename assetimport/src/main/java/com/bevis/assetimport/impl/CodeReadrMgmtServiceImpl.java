package com.bevis.assetimport.impl;

import com.bevis.assetimport.CodeReadrMgmtService;
import com.bevis.assetimport.domain.CodeReadrService;
import com.bevis.assetimport.repository.CodeReadrServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.bevis.assetimport.specification.CodeReadrServiceSpecification.bySearchQuery;

@Service
@Transactional
@RequiredArgsConstructor
public class CodeReadrMgmtServiceImpl implements CodeReadrMgmtService {
    private final CodeReadrServiceRepository codeReadrServiceRepository;

    @Override
    public Page<CodeReadrService> searchAll(String search, Pageable pageable) {
        return codeReadrServiceRepository.findAll(bySearchQuery(search), pageable);
    }

    @Override
    public Optional<CodeReadrService> findById(Long id) {
        return codeReadrServiceRepository.findById(id);
    }

    @Override
    public CodeReadrService save(CodeReadrService codeReadrService) {
        return codeReadrServiceRepository.save(codeReadrService);
    }

    @Override
    public void deleteById(Long id) {
        codeReadrServiceRepository.deleteById(id);
    }
}
