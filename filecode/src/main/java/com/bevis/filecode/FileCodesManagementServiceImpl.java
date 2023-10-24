package com.bevis.filecode;

import com.bevis.filecode.domain.FileCode;
import com.bevis.filecode.repository.FileCodesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.bevis.filecode.FileCodesSpecification.bySearchQuery;

@Service
@Transactional
@RequiredArgsConstructor
class FileCodesManagementServiceImpl implements FileCodesManagementService {

    private final FileCodesRepository fileCodesRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<FileCode> findById(String id) {
        return fileCodesRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<FileCode> findAll(Pageable pageable) {
        return fileCodesRepository.findAll(pageable);
    }

    @Override
    public Page<FileCode> searchAll(String search, Pageable pageable) {
        return fileCodesRepository.findAll(bySearchQuery(search), pageable);
    }

    @Override
    public FileCode save(FileCode fileCode) {
        return fileCodesRepository.save(fileCode);
    }

    @Override
    public void delete(String id) {
        fileCodesRepository.deleteById(id);
    }
}
