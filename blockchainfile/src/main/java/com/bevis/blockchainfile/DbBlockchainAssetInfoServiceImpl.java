package com.bevis.blockchainfile;

import com.bevis.blockchainfile.dto.AssetFilesDTO;
import com.bevis.blockchainfile.dto.FileDTO;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.filecore.domain.File;
import com.bevis.filecode.domain.FileCode;
import com.bevis.master.domain.Master;
import com.bevis.filecore.repository.FileRepository;
import com.bevis.filecode.FileCodeService;
import com.bevis.master.MasterService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.bevis.common.util.StringUtil.toUpperCase;

@Service
@RequiredArgsConstructor
class DbBlockchainAssetInfoServiceImpl implements BlockchainAssetInfoService {

    private final MasterService masterService;
    private final FileRepository fileRepository;
    private final BlockchainFileMapper blockchainFileMapper;
    private final FileCodeService fileCodeService;

    @Override
    public AssetFilesDTO getFilesByAssetIdOrPublicKey(String assetIdOrPublicKey) {
        Master master = masterService.findFirstByPublicKeyOrId(assetIdOrPublicKey, assetIdOrPublicKey)
                .orElseThrow(() -> new ObjectNotFoundException("Master with assetId (public key) " + assetIdOrPublicKey + " not found"));
        return loadAssetFilesByMaster(master);
    }

    private AssetFilesDTO loadAssetFilesByMaster(Master master) {
        File certificateFile = master.getCertificate();

        Map<String, FileCode> fileCodes = fileCodeService.getFileCodesMap();

        final FileDTO certificate = blockchainFileMapper.mapFile(certificateFile, fileCodes.get(getFileType(certificateFile)));

        Map<String, Integer> priorityCodes = fileCodeService.getPriorityCodesMap();


        List<FileDTO> files = fileRepository.findAllByAssetId(master.getId()).stream()
                .filter(x -> !Objects.equals(x, certificateFile))
                .sorted((f1, f2) ->
                        Objects.compare(
                                priorityCodes.getOrDefault(toUpperCase(f1.getFileType()), 0),
                                priorityCodes.getOrDefault(toUpperCase(f2.getFileType()), 0),
                                Comparator.comparing(e -> e)
                        )
                )
                .map(file -> blockchainFileMapper.mapFile(file, fileCodes.get(getFileType(file))))
                .collect(Collectors.toList());
        return new AssetFilesDTO(certificate, files);
    }

    @Nullable
    private String getFileType(File file) {
        return Objects.nonNull(file) ? toUpperCase(file.getFileType()) : null;
    }
}
