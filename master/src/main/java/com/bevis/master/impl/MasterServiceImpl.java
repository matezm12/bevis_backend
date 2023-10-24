package com.bevis.master.impl;

import com.bevis.common.exception.BaseException;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.common.async.TransactionalAsyncService;
import com.bevis.filecore.domain.File;
import com.bevis.master.domain.Master;
import com.bevis.master.repository.MasterRepository;
import com.bevis.master.MasterService;
import com.bevis.master.dto.MasterPartUpdate;
import com.bevis.master.dto.SearchMasterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.bevis.master.dto.SearchMasterRequest.SKU;
import static com.bevis.master.specification.MasterSpecification.byAnonymousSearchMasterRequest;
import static com.bevis.master.specification.MasterSpecification.bySearchMasterRequest;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
class MasterServiceImpl implements MasterService {

    private final MasterRepository masterRepository;
    private final TransactionalAsyncService asyncService;

    @Transactional(readOnly = true)
    @Override
    public boolean exists(String publicKey) {
        return masterRepository.existsByIdOrPublicKey(publicKey, publicKey);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Master> findById(String assetId) {
        return masterRepository.findById(assetId);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Master> findOne(SearchMasterRequest params) {
        return masterRepository.findOne(byAnonymousSearchMasterRequest(params));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Master> findFirstByPublicKey(String publicKey) {
        return masterRepository.findFirstByPublicKey(publicKey);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Master> findFirstByPublicKeyOrId(String publicKey, String id) {
        return masterRepository.findFirstByPublicKeyOrId(publicKey, id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Master> searchMaster(SearchMasterRequest masterRequest, Pageable pageable) {
        Sort sort = pageable.getSort();
        PageRequest newPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return masterRepository.findAll(bySearchMasterRequest(masterRequest, sort), newPageable);
    }

    @Override
    public List<Master> searchMaster(SearchMasterRequest masterRequest) {
        return masterRepository.findAll(bySearchMasterRequest(masterRequest));
    }

    @Override
    public List<Master> findAll() {
        return masterRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Master> findAllByPublicKeys(List<String> publicKeys) {
        return masterRepository.findAllByPublicKeyIn(publicKeys);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Master> findAllByPublicKeyIn(List<String> publicKeys) {
        return masterRepository.findAllByPublicKeyIn(publicKeys);
    }

    @Override
    public List<Master> findAllByAssetIdIn(List<String> assetIDs) {
        return masterRepository.findAllByIdIn(assetIDs);
    }

    @Override
    public List<Master> findAllByAssetIdOrPublicKeyIdIn(List<String> assetIDsOrPublicKeys) {
        return masterRepository.findAllByIdInOrPublicKeyIn(assetIDsOrPublicKeys, assetIDsOrPublicKeys);
    }

    @Override
    public List<Master> findAllByScanId(String scanId) {
        return masterRepository.findAllByCodereadrScanId(scanId);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Master> findByIdOrPublicKey(String assetIdOrPublicKey) {
        return masterRepository.findFirstByPublicKeyOrId(assetIdOrPublicKey, assetIdOrPublicKey);
    }

    @Override
    public Master saveMaster(Master master) {
        return masterRepository.save(master);
    }

    @Override
    public Master updateMaster(MasterPartUpdate partUpdate) {
        if (partUpdate.getId() == null) {
            throw new BaseException("Invalid id");
        }
        Master master = masterRepository.findById(partUpdate.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Master with ID: " + partUpdate.getId() + " not found"));
        master.setAssetType(partUpdate.getAssetType());
        if (Objects.nonNull(partUpdate.getGenDate())) {
            master.setGenDate(partUpdate.getGenDate());
        }
        if (Objects.nonNull(partUpdate.getIsLocked())) {
            master.setIsLocked(partUpdate.getIsLocked());
        }
        if (Objects.nonNull(partUpdate.getIsCsc())) {
            master.setIsCsc(partUpdate.getIsCsc());
        }
        if (Objects.nonNull(partUpdate.getIsActive())) {
            master.setIsActive(partUpdate.getIsActive());
        }
        if (Objects.nonNull(partUpdate.getOwnerAssetId())) {
            master.setOwnerAssetId(partUpdate.getOwnerAssetId());
        }
        return masterRepository.save(master);
    }

    @Override
    public void update(Master master) {
        masterRepository.saveAndFlush(master);
    }

    @Override
    public void deleteById(String assetId) {
        masterRepository.deleteById(assetId);
    }

    @Override
    public void saveAndFlush(Master master) {
        masterRepository.saveAndFlush(master);
    }

    @Override
    public void saveAll(List<Master> masters) {
        masterRepository.saveAll(masters);
    }

    @Override
    public void updatePrimaryFile(String assetId, File file, boolean updateChildren) {
        assert Objects.equals(assetId, file.getAssetId());
        Master master = findById(assetId).orElseThrow(ObjectNotFoundException::new);
        master.setFile(file);
        saveAndFlush(master);
        if (updateChildren) {
            updatePrimaryFileForChildren(assetId, file);
        }
    }

    @Override
    public Master deactivateMaster(String assetId) {
        Master master = findById(assetId).orElseThrow(ObjectNotFoundException::new);
        master.setIsActive(false);
        return masterRepository.save(master);
    }

    private void updatePrimaryFileForChildren(String assetId, File file) {
        asyncService.runTxAfterCommit(() -> {
            SearchMasterRequest request = new SearchMasterRequest();
            request.setDynamicFilter(Collections.singletonMap(SKU, assetId));
            masterRepository.findAll(bySearchMasterRequest(request))
                    .forEach(x -> x.setFile(file));
        });
    }
}
