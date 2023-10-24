package com.bevis.filecore;

import com.bevis.filecore.domain.File;
import com.bevis.filecore.dto.FileFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FileService {

    Optional<File> findById(Long id);

    Page<File> searchFile(FileFilter filter, Pageable pageable);

    List<File> findAllUnconfirmed();

    File save(File file);

    File updateFileName(File file);
}
