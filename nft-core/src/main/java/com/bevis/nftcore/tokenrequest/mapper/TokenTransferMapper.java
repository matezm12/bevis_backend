package com.bevis.nftcore.tokenrequest.mapper;

import com.bevis.nftcore.domain.TokenTransfer;
import com.bevis.nftcore.tokenrequest.dto.TokenTransferDTO;
import org.mapstruct.Mapper;

@Mapper
public interface TokenTransferMapper {
    TokenTransferDTO toDto(TokenTransfer tokenTransfer);
}
