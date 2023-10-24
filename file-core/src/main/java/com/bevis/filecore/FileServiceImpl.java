package com.bevis.filecore;

import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.filecore.domain.File;
import com.bevis.filecore.repository.FileRepository;
import com.bevis.filecore.dto.FileFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Override
    public Optional<File> findById(Long id) {
        return fileRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<File> searchFile(FileFilter filter, Pageable pageable) {
        return fileRepository.findAll(FileSearchSpecification.bySearchQuery(filter), pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<File> findAllUnconfirmed() {
        return fileRepository.findAllByBlockIsNull();
    }

    @Override
    public File save(File file) {
        return fileRepository.saveAndFlush(file);
    }

    @Override
    public File updateFileName(File file) {
        File currentFile = fileRepository.findById(file.getId())
                .orElseThrow(ObjectNotFoundException::new);
        currentFile.setFileName(file.getFileName());
        return fileRepository.save(currentFile);
    }
}
