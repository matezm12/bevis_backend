package com.bevis.nftcore.tokenrequest;

import com.bevis.blockchaincore.BlockchainUrlGateway;
import com.bevis.blockchaincore.domain.Blockchain;
import com.bevis.nftcore.domain.TokenTransfer;
import com.bevis.nftcore.repository.TokenTransferRepository;
import com.bevis.gateway.UrlGatewayService;
import com.bevis.nftcore.tokenrequest.dto.TokenTransferDTO;
import com.bevis.nftcore.tokenrequest.dto.TokenTransferFilterDTO;
import com.bevis.nftcore.tokenrequest.mapper.TokenTransferMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.bevis.nftcore.tokenrequest.specification.TokenTransferSearchSpecification.byFilterRequest;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
class TokenTransferServiceImpl implements TokenTransferService {

    private final TokenTransferRepository tokenTransferRepository;
    private final TokenTransferMapper tokenTransferMapper;
    private final UrlGatewayService urlGatewayService;
    private final BlockchainUrlGateway blockchainUrlGateway;

    @Override
    public Page<TokenTransferDTO> findAll(TokenTransferFilterDTO filter, Pageable pageable) {
        return tokenTransferRepository.findAll(byFilterRequest(filter), pageable)
                .map(this::mapTokenTransfer);
    }

    @Override
    public Integer countProcessedByRequestId(Long tokenRequestId) {
        return tokenTransferRepository.countByTokenRequestIdAndTransactionIdNotNull(tokenRequestId);
    }

    @Override
    public Integer countFailedByRequestId(Long tokenRequestId) {
        return tokenTransferRepository.countByTokenRequestIdAndErrorMessageNotNull(tokenRequestId);
    }

    @Override
    public List<TokenTransfer> saveAll(Iterable<TokenTransfer> tokenTransfers) {
        return tokenTransferRepository.saveAllAndFlush(tokenTransfers);
    }

    @Override
    public List<TokenTransfer> findAllByRequestId(Long id) {
        return tokenTransferRepository.findAllByTokenRequestId(id);
    }

    @Override
    public List<TokenTransfer> findAllFailedByRequestId(Long id) {
        return tokenTransferRepository.findAllByTokenRequestIdAndErrorMessageNotNull(id);
    }

    private TokenTransferDTO mapTokenTransfer(TokenTransfer tokenTransfer) {
        TokenTransferDTO tokenTransferDTO = tokenTransferMapper.toDto(tokenTransfer);
        if (Objects.nonNull(tokenTransfer.getTokenRequest()) && Objects.nonNull(tokenTransfer.getTokenRequest().getBlockchain())) {
            Blockchain blockchain = tokenTransfer.getTokenRequest().getBlockchain();
            tokenTransferDTO.setAddressLink(blockchainUrlGateway.getBlockchainAddressLink(tokenTransfer.getDestinationAddress(), blockchain));
            if (Objects.nonNull(tokenTransfer.getTransactionId())) {
                tokenTransferDTO.setTransactionLink(blockchainUrlGateway.getBlockchainTransactionLink(tokenTransfer.getTransactionId(), blockchain));
            }
        }
        return tokenTransferDTO;
    }
}
