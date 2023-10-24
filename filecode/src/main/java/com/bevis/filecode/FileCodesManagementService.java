package com.bevis.filecode;

import com.bevis.filecode.domain.FileCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface FileCodesManagementService {

    Optional<FileCode> findById(String id);

    Page<FileCode> findAll(Pageable pageable);

    Page<FileCode> searchAll(String search, Pageable pageable);

    FileCode save(FileCode fileCode);

    void delete(String id);
}
