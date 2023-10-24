package com.bevis.webapp;

import com.bevis.assetinfo.dto.AssetGroupsInfoWrapper;
import com.bevis.balance.dto.CryptoToken;
import com.bevis.blockchainfile.dto.AssetFilesDTO;
import com.bevis.coininfo.dto.CoinDetails;
import com.bevis.nft.nfttoken.dto.NftDto;
import com.bevis.webapp.dto.AssetIdDto;
import com.bevis.webapp.masterdetails.dto.MasterDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AssetDetailsService {
    List<MasterDetailsDTO> getAssetsByIDs(List<AssetIdDto> assetIds);

    MasterDetailsDTO getMasterDetails(String assetId);

    AssetGroupsInfoWrapper getFullAssetInfo(String assetId);

    AssetFilesDTO getAssetFiles(String assetId);

    Page<MasterDetailsDTO> findMasterParents(String assetId, Pageable pageable);

    Page<MasterDetailsDTO> findMasterChildren(String assetId, Pageable pageable);

    Page<CryptoToken> getCoinTokens(String assetId);

    Page<NftDto> getCoinNftTokens(String assetId);

    CoinDetails getCoinDetails(String coinId, String fiatCurrency);
}
