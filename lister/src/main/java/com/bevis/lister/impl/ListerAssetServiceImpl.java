package com.bevis.lister.impl;

import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.events.dto.user.UserDataDeleteEvent;
import com.bevis.master.domain.Master;
import com.bevis.user.domain.User;
import com.bevis.lister.*;
import com.bevis.lister.dto.ListerAssetRequest;
import com.bevis.lister.dto.ListerAssetResponse;
import com.bevis.master.MasterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bevis.lister.impl.ListerAssetSpecification.bySearchQuery;

@Slf4j
@Service
@RequiredArgsConstructor
class ListerAssetServiceImpl implements ListerAssetService {

    private final ListerAssetRepository listerAssetRepository;
    private final ListerAssetInfoService listerAssetMapper;
    private final MasterService masterService;

    @Override
    public ListerAssetResponse findOne(String assetId, User user) {
        return listerAssetMapper.toDTO(getAsset(assetId, user));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ListerAssetResponse> getUserList(User user, Pageable pageable, String displayCurrency) {
        return listerAssetMapper.toDto(listerAssetRepository.findAllByUser(user, pageable), displayCurrency);
    }

    @Override
    public Page<ListerAssetResponse> getUserList(User user, String search, Pageable pageable, String displayCurrency) {
        return listerAssetMapper.toDto(listerAssetRepository.findAll(bySearchQuery(search, user), pageable), displayCurrency);
    }

    @Transactional
    @Override
    public ListerAssetResponse addToUserList(ListerAssetRequest assetRequest, User user) {
        if (listerAssetRepository.existsByMasterIdAndUser(assetRequest.getAssetId(), user)){
            return listerAssetMapper.toDTO(listerAssetRepository.findFirstByMasterIdAndUser(assetRequest.getAssetId(), user)
                    .orElseThrow(() -> new ListerAssetException("Asset not exist in list")));
        }
        ListerAsset listerAsset = new ListerAsset();
        Master master = getMaster(assetRequest.getAssetId());
        listerAsset.setMaster(master);
        listerAsset.setName(assetRequest.getName());
        listerAsset.setUser(user);
        listerAssetRepository.save(listerAsset);
        return listerAssetMapper.toDTO(listerAsset);
    }

    @Transactional
    @Override
    public void removeFromUserList(String assetId, User user) {
        listerAssetRepository.deleteByMasterAndUser(getMaster(assetId), user);
    }

    @Transactional
    @Override
    public ListerAssetResponse renameForUser(String assetId, String newName, User user) {
        ListerAsset listerAsset = getAsset(assetId, user);
        listerAsset.setName(newName);
        listerAssetRepository.save(listerAsset);
        return listerAssetMapper.toDTO(listerAsset);
    }

    @EventListener
    @Order(1)
    @Override
    public void handleUserDataDeleteEvent(UserDataDeleteEvent e) {
        log.debug("Deleting Lister User data. [Order 1.]");
        listerAssetRepository.deleteAllByUserId(e.getUserId());
    }

    private ListerAsset getAsset(String assetId, User user) {
        return listerAssetRepository.findOneByMasterIdAndUser(assetId, user).orElseThrow(() -> new ObjectNotFoundException("Asset not found in list"));
    }

    private Master getMaster(String assetId) {
        return masterService.findByIdOrPublicKey(assetId)
                .orElseThrow(() -> new ObjectNotFoundException("Asset not exists"));
    }
}
