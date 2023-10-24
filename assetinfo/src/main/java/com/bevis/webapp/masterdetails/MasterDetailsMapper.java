package com.bevis.webapp.masterdetails;

import com.bevis.assettype.domain.AssetType;
import com.bevis.blockchaincore.BlockchainUrlGateway;
import com.bevis.blockchaincore.domain.Blockchain;
import com.bevis.filecore.domain.File;
import com.bevis.user.domain.User;
import com.bevis.gateway.UrlGatewayService;
import com.bevis.master.domain.Master;
import com.bevis.master.domain.MasterImport;
import com.bevis.webapp.masterdetails.dto.AssetTypeDTO;
import com.bevis.webapp.masterdetails.dto.BlockchainDTO;
import com.bevis.webapp.masterdetails.dto.FileDTO;
import com.bevis.webapp.masterdetails.dto.MasterDetailsDTO;
import com.bevis.webapp.masterdetails.dto.MasterImportDTO;
import com.bevis.webapp.masterdetails.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
abstract class MasterDetailsMapper {

    @Autowired
    protected UrlGatewayService urlGatewayService;

    @Autowired
    protected BlockchainUrlGateway blockchainUrlGateway;

    @Mapping(target = "assetId", source = "id")
    @Mapping(
            target="publicKeyUrl",
            expression="java(blockchainUrlGateway.getBlockchainAddressLink(master.getPublicKey(), master.getBlockchain()))"
    )
    abstract MasterDetailsDTO mapMasterDetails(Master master);

    @Mapping(target="ipfsIcon", expression="java(urlGatewayService.getIpfsUrl(blockchain.getIpfsIcon()))")
    abstract BlockchainDTO mapBlockchain(Blockchain blockchain);

    abstract MasterImportDTO mapMasterImport(MasterImport masterImport);

    abstract AssetTypeDTO mapAssetType(AssetType assetType);

    @Mapping(target="ipfsUrl", expression="java(urlGatewayService.getIpfsUrl(file.getIpfs()))")
    @Mapping(
            target="transactionUrl",
            expression="java(blockchainUrlGateway.getBlockchainTransactionLink(file.getTransactionId(), file.getBlockchain()))"
    )
    abstract FileDTO mapFile(File file);

    abstract UserDTO mapUser(User user);
}
