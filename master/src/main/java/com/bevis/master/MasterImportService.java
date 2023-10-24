package com.bevis.master;

import com.bevis.assettype.domain.AssetType;
import com.bevis.master.domain.Master;
import com.bevis.master.domain.MasterImport;
import com.bevis.master.dto.MasterImportCsvDataDTO;
import com.bevis.master.dto.MasterImportDTO;
import com.bevis.master.dto.MasterImportRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MasterImportService {

    MasterImportDTO generateMasters(MasterImportRequest request);

    MasterImportDTO updateMasters(MasterImportRequest request);

    MasterImportCsvDataDTO getMasterImportAssetKeys(Long masterImportId);

    Master generateMasterOnDefaultBlockchain();

    Master generateBevisMasterByBlockchain(String blockchain);

    Master generateMasterByBlockchain(String blockchainName, AssetType assetType);

    Page<MasterImport> findAll(Pageable pageable);

    Page<MasterImport> findAllByNameContaining(String search, Pageable pageable);

    MasterImport getOne(Long id);
}
