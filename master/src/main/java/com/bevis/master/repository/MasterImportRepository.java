package com.bevis.master.repository;

import com.bevis.master.domain.MasterImport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MasterImportRepository extends JpaRepository<MasterImport, Long> {
    Page<MasterImport> findAllByNameContaining(String search, Pageable pageable);
}
