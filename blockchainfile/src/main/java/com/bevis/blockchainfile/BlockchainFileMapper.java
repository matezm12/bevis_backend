package com.bevis.blockchainfile;

import com.bevis.blockchaincore.BlockchainUrlGateway;
import com.bevis.blockchainfile.dto.FileDTO;
import com.bevis.filecore.domain.File;
import com.bevis.filecode.domain.FileCode;
import com.bevis.gateway.UrlGatewayService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@Mapper
abstract class BlockchainFileMapper {

    protected UrlGatewayService urlGatewayService;
    protected BlockchainUrlGateway blockchainUrlGateway;

    @Autowired
    public void setUrlGatewayService(UrlGatewayService urlGatewayService, BlockchainUrlGateway blockchainUrlGateway) {
        this.urlGatewayService = urlGatewayService;
        this.blockchainUrlGateway = blockchainUrlGateway;
    }

    @Mapping(source = "fileType", target = "fileType", qualifiedByName = "toUpperCase")
    @Mapping(source = "ipfs", target = "ipfsHash")
    @Mapping(target = "url", expression = "java(urlGatewayService.getIpfsUrl(file.getIpfs()))")
    @Mapping(target = "txUrl",
            expression = "java(blockchainUrlGateway.getBlockchainTransactionLink(file.getTransactionId(), file.getBlockchain()))")
    abstract FileDTO mapFile(File file);

    @Mapping(target = "fileType", source = "file.fileType", qualifiedByName = "toUpperCase")
    @Mapping(target = "ipfsHash", source = "file.ipfs")
    @Mapping(target = "url", expression = "java(urlGatewayService.getIpfsUrl(file.getIpfs()))")
    @Mapping(target = "txUrl",
            expression = "java(blockchainUrlGateway.getBlockchainTransactionLink(file.getTransactionId(), file.getBlockchain()))")
    @Mapping(target = "fileGroup", source = "fileCode.fileGroup")
    @Mapping(target = "placeholderUrl", expression = "java(mapPlaceholder(file, fileCode))")
    abstract FileDTO mapFile(File file, FileCode fileCode);

    @Named("mapPlaceholder")
    String mapPlaceholder(File file, FileCode fileCode) {
        if (Objects.nonNull(file)) {
            if (Objects.nonNull(fileCode)) {
                String ipfs = file.getEncrypted() ? fileCode.getEncryptedPlaceholder() : fileCode.getPlaceholder();
                return urlGatewayService.getIpfsUrl(ipfs);
            }
            return "";
        }
        return null;
    }

    @Named("toUpperCase")
    String toUpperCase(String s) {
        if (s == null) {
            return s;
        }
        return s.toUpperCase();
    }
}
