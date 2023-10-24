package com.bevis.assetinfo.impl;

import com.bevis.assetinfo.AdminAssetGroupsInfoService;
import com.bevis.assetinfo.AssetGroupsInfoService;
import com.bevis.assetinfo.dto.AssetGroupsInfoWrapper;
import com.bevis.assetinfo.mapper.AssetInfoGroupMapper;
import com.bevis.blockchainfile.BlockchainAssetInfoService;
import com.bevis.blockchainfile.dto.FileDTO;
import com.bevis.master.domain.Master;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdminAssetGroupsInfoServiceImpl implements AdminAssetGroupsInfoService {
    private final AssetGroupsInfoService assetGroupsInfoService;
    private final BlockchainAssetInfoService blockchainAssetInfoService;
    private final AssetInfoGroupMapper assetInfoGroupMapper;


    @Override
    public AssetGroupsInfoWrapper getAssetInfo(String assetId) {
        AssetGroupsInfoWrapper assetGroupsInfoWrapper = assetInfoGroupMapper.map(assetGroupsInfoService.getAssetInfo(assetId));
        assetGroupsInfoWrapper.getAssetGroups()
                .forEach(x -> x.setFiles(loadFiles(x.getGroupAssetId())));
        return assetGroupsInfoWrapper;
    }

    @Override
    public AssetGroupsInfoWrapper getAssetInfo(Master master) {
        AssetGroupsInfoWrapper assetGroupsInfoWrapper = assetInfoGroupMapper.map(assetGroupsInfoService.getAssetInfo(master));
        assetGroupsInfoWrapper.getAssetGroups()
                .forEach(x -> x.setFiles(loadFiles(x.getGroupAssetId())));
        return assetGroupsInfoWrapper;
    }

    private List<FileDTO> loadFiles(String groupAssetId) {
        return Optional.ofNullable(groupAssetId)
                .map(blockchainAssetInfoService::getFilesByAssetIdOrPublicKey)
                .map(Collections::singletonList)
                .orElse(Collections.emptyList())
                .stream()
                .flatMap(x -> Stream.concat(
                        Stream.of(x.getFiles()),
                        Stream.of(Collections.singletonList(x.getCertificate()))
                ))
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
