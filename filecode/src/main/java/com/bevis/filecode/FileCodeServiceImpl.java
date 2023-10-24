package com.bevis.filecode;

import com.bevis.common.util.StringUtil;
import com.bevis.filecode.domain.FileCode;
import com.bevis.filecode.repository.FileCodesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
class FileCodeServiceImpl implements FileCodeService {

    private final FileCodesRepository fileCodesRepository;

    @Override
    public String getCodeByFileType(String fileType) {
        FileCode fileCode = fileCodesRepository.findById(fileType)
                .orElseThrow(() -> new FileCodeException("File type not found"));
        return fileCode.getPriorityCode();
    }

    @Override
    public Optional<FileCode> getFileCodeByFileType(String fileType) {
        return fileCodesRepository.findById(fileType);
    }

    @Override
    public Set<String> getSupportedExtensions() {
        return fileCodesRepository.findAll()
                .stream()
                .map(FileCode::getFileType)
                .map(StringUtil::toUpperCase)
                .collect(Collectors.toSet());
    }

    @Override
    public Map<String, Integer> getPriorityCodesMap() {
        return fileCodesRepository.findAll()
                .stream()
                .collect(Collectors.toMap(fc1 -> fc1.getFileType().toUpperCase().trim(),
                        fc2 -> Integer.parseInt(fc2.getPriorityCode())));
    }

    @Override
    public Map<String, FileCode> getFileCodesMap() {
        return fileCodesRepository.findAll()
                .stream()
                .collect(Collectors.toMap(fc1 -> fc1.getFileType().toUpperCase().trim(), fc2 -> fc2));
    }
}
