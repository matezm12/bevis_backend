package com.bevis.coininfo.impl;

import com.bevis.assetinfo.AssetGroupsInfoService;
import com.bevis.assetinfo.LogoFetchingService;
import com.bevis.assetinfo.dto.AssetGroup;
import com.bevis.assetinfo.dto.AssetValue;
import com.bevis.assetinfo.mapper.AssetInfoMapper;
import com.bevis.balance.dto.Balance;
import com.bevis.balancecore.CryptoBalanceLoader;
import com.bevis.coininfo.CoinDetailsService;
import com.bevis.coininfo.CoinInfoService;
import com.bevis.coininfo.CscMasterLoader;
import com.bevis.coininfo.dto.Coin;
import com.bevis.coininfo.dto.CoinDetails;
import com.bevis.coininfo.dto.CoinDetailsItem;
import com.bevis.coininfo.dto.CoinRequest;
import com.bevis.coininfo.mapper.CoinDetailsMapper;
import com.bevis.blockchaincore.domain.Blockchain;
import com.bevis.master.domain.Master;
import com.bevis.gateway.UrlGatewayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.bevis.assetinfo.util.MasterFetchUtil.getMasterCurrency;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
class CoinDetailsServiceImpl implements CoinDetailsService {

    private static final String ASSET_ID_FIELD = "asset_id";

    private static final String TITLE_NOT_AVAILABLE = "N/A";
    private static final List<String> DISPLAY_GROUPS = Arrays.asList("Sku", "Shipment", "Location");

    private final CscMasterLoader cscMasterLoader;
    private final UrlGatewayService urlGatewayService;
    private final CoinInfoService coinInfoService;
    private final AssetGroupsInfoService assetGroupsInfoService;
    private final LogoFetchingService logoFetchingService;
    private final CryptoBalanceLoader cryptoBalanceLoader;
    private final AssetInfoMapper assetInfoMapper;
    private final CoinDetailsMapper coinDetailsMapper;

    @Override
    public CoinDetails getCoinDetails(CoinRequest coinRequest, String fiatCurrency) {
        Master master = cscMasterLoader.getCscMaster(coinRequest.getPublicKey());
        master.setActivationStatus(true);

        Double balance = Optional.ofNullable(coinRequest.getCryptoBalance())
                .orElseGet(() -> loadCryptoBalance(getMasterCurrency(master), master.getPublicKey()));

        Coin coin = coinInfoService.loadCoin(master, fiatCurrency, balance);

        List<AssetGroup> assetGroups = assetGroupsInfoService.loadAssetGroups(master);

        if (Objects.nonNull(coin)) {
            coin.setLogo(logoFetchingService.loadFileUrlByMaster(master));
        }

        List<CoinDetailsItem> displayValues = new ArrayList<>(getCoinDetailsItems(master, assetGroups));

        List<AssetValue> assetValues = getAssetValuesByGroups(assetGroups, DISPLAY_GROUPS);
        displayValues.addAll(coinDetailsMapper.mapDynamicValues(assetValues));

        return CoinDetails.of(coin, displayValues);
    }

    //FIXME FIX THIS
    @NotNull
    private List<CoinDetailsItem> getCoinDetailsItems(Master master, List<AssetGroup> assetGroups) {
        List<CoinDetailsItem> displayValues = new ArrayList<>();

        displayValues.add(mapAsset("Coin ID", master.getId()));
        displayValues.add(mapSimpleValue("Public Key", master.getPublicKey(), assetInfoMapper.getBlockchainAddressLink(master)));
        displayValues.add(mapSimpleValue("Generation Date", master.getGenDate().toString()));
        displayValues.add(mapSimpleValue("Blockchain", mapBlockchain(master.getBlockchain())));
        displayValues.add(mapSimpleValue("Currency", mapCurrency(master.getBlockchain())));

        findValueInGroup(assetGroups, "Carton", ASSET_ID_FIELD)
                .ifPresent(carton -> displayValues.add(mapSimpleValue("Carton", carton)));
        return displayValues;
    }

    @NotNull
    private List<AssetValue> getAssetValuesByGroups(List<AssetGroup> assetGroups, List<String> groups) {
        return groups.stream()
                .flatMap(group -> findGroup(assetGroups, group).stream())
                .collect(Collectors.toList());
    }

    private List<AssetValue> findGroup(List<AssetGroup> assetGroups, String groupName) {
        return assetGroups.stream()
                .filter(x -> Objects.equals(x.getGroupName(), groupName))
                .findFirst()
                .map(AssetGroup::getAssetValues)
                .orElse(Collections.emptyList());
    }

    private Optional<String> findValueInGroup(List<AssetGroup> assetGroups, String groupName, String fieldName) {
        return assetGroups.stream()
                .filter(x -> Objects.equals(x.getGroupName(), groupName))
                .findFirst()
                .map(AssetGroup::getAssetValues)
                .orElse(Collections.emptyList())
                .stream()
                .filter(x -> Objects.equals(x.getFieldName(), fieldName))
                .findFirst()
                .map(AssetValue::getFieldValue);
    }

    private String mapBlockchain(Blockchain blockchain) {
        return Optional.ofNullable(blockchain)
                .map(Blockchain::getFullName)
                .map(String::toUpperCase)
                .filter(x -> !Strings.isBlank(x))
                .orElse(TITLE_NOT_AVAILABLE);
    }

    private String mapCurrency(Blockchain blockchain) {
        return Optional.ofNullable(blockchain)
                .map(Blockchain::getCurrencyCode)
                .map(String::toUpperCase)
                .filter(x -> !Strings.isBlank(x))
                .orElse(TITLE_NOT_AVAILABLE);
    }

    private CoinDetailsItem mapSimpleValue(String title, String value) {
        return CoinDetailsItem.of(title, value);
    }

    private CoinDetailsItem mapSimpleValue(String title, String value, String link) {
        return new CoinDetailsItem(title, value, link, false);
    }

    private CoinDetailsItem mapAsset(String title, String assetId) {
        return CoinDetailsItem.builder()
                .fieldTitle(title)
                .fieldValue(assetId)
                .link(urlGatewayService.getAssetViewerLink(assetId))
                .isAsset(true)
                .build();
    }

    private Double loadCryptoBalance(String cryptoCurrency, String publicKey) {
        if (Objects.isNull(cryptoCurrency) || Objects.isNull(publicKey)) {
            log.warn("Could not load crypto balance. cryptoCurrency: {}, publicKey: {}", cryptoCurrency, publicKey);
            return null;
        }
        try {
            Balance walletBalance = cryptoBalanceLoader.getWalletBalance(publicKey, cryptoCurrency);
            return Balance.getRealBalance(walletBalance);
        } catch (Exception e) {
            log.error("Could not crypto load balance. Crypto currency: {}, publicKey: {}", cryptoCurrency, publicKey);
            log.error(e.getMessage());
            return null;
        }
    }
}
