package com.bevis.webapp.masterdetails;

import com.bevis.master.domain.Master;
import com.bevis.webapp.dto.AssetIdDto;
import com.bevis.webapp.masterdetails.dto.MasterDetailsDTO;

import java.util.List;

public interface MasterDetailsService {
    List<MasterDetailsDTO> getAssetsByIDs(List<AssetIdDto> assetIds);
    MasterDetailsDTO getMasterDetails(String assetId);
    MasterDetailsDTO getMasterDetails(Master master);
}
