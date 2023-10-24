package com.bevis.gateway;

import com.bevis.config.ConfigKey;
import com.bevis.config.ConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Objects;

import static com.bevis.common.util.StringUtil.isUrl;

@Slf4j
@Service
@RequiredArgsConstructor
class UrlGatewayServiceImpl implements UrlGatewayService {

    private final ConfigService configService;

    private String ipfsLinkTemplate;
    private String assetViewerLinkTemplate;
    private String googleMapGpsLinkTemplate;
    private String carrierTrackingLinkTemplate;

    @PostConstruct
    void init() {
        ipfsLinkTemplate = configService.getValue(ConfigKey.IPFS_LINK_TEMPLATE);
        assetViewerLinkTemplate = configService.getAssetViewerLinkTemplate();
        googleMapGpsLinkTemplate = configService.getValue(ConfigKey.GOOGLE_MAP_GPS_LINK_TEMPLATE);
        carrierTrackingLinkTemplate = configService.getValue(ConfigKey.CARRIER_TRACKING_LINK_TEMPLATE);
    }

    @Override
    public String getIpfsUrl(String cid) {
        if (Objects.nonNull(cid) && !isUrl(cid)) {
            String ipfsTemplate = ipfsLinkTemplate;
            return String.format(ipfsTemplate, cid);
        }
        return cid;
    }

    @Override
    public String getAssetViewerLink(String assetId) {
        if (Objects.isNull(assetId))
            return Strings.EMPTY;
        return String.format(assetViewerLinkTemplate, assetId);
    }

    @Override
    public String getGoogleMapLink(String gps) {
        if (Objects.isNull(gps))
            return Strings.EMPTY;
        return String.format(googleMapGpsLinkTemplate, gps);
    }

    @Override
    public String getCarrierTrackingLink(String carrier, String trackingNumber) {
        if (Objects.isNull(carrier) || Objects.isNull(trackingNumber))
            return Strings.EMPTY;
        return String.format(carrierTrackingLinkTemplate, carrier, trackingNumber);
    }
}
