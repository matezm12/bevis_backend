package com.bevis.master;

import com.bevis.filecore.domain.File;
import com.bevis.master.domain.Master;
import com.bevis.master.dto.MasterPartUpdate;
import com.bevis.master.dto.SearchMasterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MasterService {

    boolean exists(String publicKey);

    Optional<Master> findById(String assetId);

    Optional<Master> findOne(SearchMasterRequest params);

    Optional<Master> findFirstByPublicKey(String publicKey);

    Optional<Master> findFirstByPublicKeyOrId(String publicKey, String id);

    Page<Master> searchMaster(SearchMasterRequest masterRequest, Pageable pageable);

    List<Master> searchMaster(SearchMasterRequest masterRequest);

    List<Master> findAll();

    List<Master> findAllByPublicKeys(List<String> publicKeys);

    List<Master> findAllByPublicKeyIn(List<String> publicKeys);

    List<Master> findAllByAssetIdIn(List<String> assetIDs);

    List<Master> findAllByAssetIdOrPublicKeyIdIn(List<String> assetIDs);

    List<Master> findAllByScanId(String scanId);

    Optional<Master> findByIdOrPublicKey(String assetIdOrPublicKey);

    Master saveMaster(Master master);

    Master updateMaster(MasterPartUpdate partUpdate);

    void update(Master master);

    void deleteById(String assetId);

    void saveAndFlush(Master master);

    void saveAll(List<Master> masters);

    void updatePrimaryFile(String assetId, File file, boolean updateChildren);

    Master deactivateMaster(String assetId);

}
