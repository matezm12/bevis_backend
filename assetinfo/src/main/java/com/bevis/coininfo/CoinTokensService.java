package com.bevis.coininfo;

import com.bevis.balance.dto.CryptoToken;
import com.bevis.nft.nfttoken.dto.NftDto;

import java.util.List;

public interface CoinTokensService {
    List<CryptoToken> getCoinTokens(String assetId);

    List<NftDto> getCoinNftTokens(String assetId);
}
