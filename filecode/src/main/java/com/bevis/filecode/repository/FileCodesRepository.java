package com.bevis.filecode.repository;

import com.bevis.filecode.domain.FileCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FileCodesRepository extends JpaRepository<FileCode, String>, JpaSpecificationExecutor<FileCode> {
}
