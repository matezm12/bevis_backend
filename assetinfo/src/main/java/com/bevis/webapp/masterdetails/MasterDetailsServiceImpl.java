package com.bevis.webapp.masterdetails;

import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.master.domain.Master;
import com.bevis.master.MasterService;
import com.bevis.webapp.dto.AssetIdDto;
import com.bevis.webapp.masterdetails.dto.MasterDetailsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MasterDetailsServiceImpl implements MasterDetailsService {

    private final MasterService masterService;
    private final MasterDetailsMapper masterDetailsMapper;

    @Override
    public List<MasterDetailsDTO> getAssetsByIDs(List<AssetIdDto> assetIds) {
        List<String> assetIdsOrPublicKeys = assetIds.stream().map(AssetIdDto::getAssetId)
                .collect(Collectors.toList());
        return masterService.findAllByAssetIdOrPublicKeyIdIn(assetIdsOrPublicKeys)
                .stream()
                .map(this::getMasterDetails)
                .collect(Collectors.toList());
    }

    @Override
    public MasterDetailsDTO getMasterDetails(String assetId) {
        Master master = masterService.findByIdOrPublicKey(assetId)
                .orElseThrow(() -> new ObjectNotFoundException("Master with assetId " + assetId + " not found "));
        return getMasterDetails(master);
    }

    @Override
    public MasterDetailsDTO getMasterDetails(Master master) {
        return masterDetailsMapper.mapMasterDetails(master);
    }
}
