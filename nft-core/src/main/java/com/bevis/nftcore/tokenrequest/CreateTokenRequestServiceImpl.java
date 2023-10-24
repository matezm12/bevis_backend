package com.bevis.nftcore.tokenrequest;

import com.bevis.blockchaincore.BlockchainService;
import com.bevis.common.exception.BaseException;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.common.async.TransactionalAsyncService;
import com.bevis.blockchaincore.domain.Blockchain;
import com.bevis.master.domain.Master;
import com.bevis.nftcore.domain.TokenRequest;
import com.bevis.nftcore.domain.TokenTransfer;
import com.bevis.nftcore.domain.enumeration.TokenRequestStatus;
import com.bevis.master.MasterService;
import com.bevis.nftcore.tokenrequest.dto.CreateTokenRequestDTO;
import com.bevis.nftcore.tokenrequest.dto.TokenRequestDetailsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
class CreateTokenRequestServiceImpl implements CreateTokenRequestService {

    private final TokenRequestService tokenRequestService;
    private final TokenTransferService tokenTransferService;
    private final BlockchainService blockchainService;
    private final MasterService masterService;
    private final TokenRequestProcessingProxyService tokenRequestProcessingProxyService;
    private final TokenRequestDetailsService tokenRequestDetailsService;
    private final TransactionalAsyncService asyncService;

    @Transactional
    @Override
    public TokenRequestDetailsDTO createTokenRequest(CreateTokenRequestDTO request) {

        Blockchain blockchain = blockchainService.findById(request.getBlockchainId())
                .orElseThrow(ObjectNotFoundException::new);

        if (!isSupportTokenRequest(blockchain)) {
            throw new BaseException("This blockchain is not support token request!");
        }

        HashSet<String> addressesSet = new HashSet<>(request.getDestinationAddresses());

        if (request.getDestinationAddresses().size() != addressesSet.size()) {
            throw new BaseException("Some addresses are duplicated!");
        }
        if (addressesSet.size() == 0) {
            throw new BaseException("Receivers not defined!");
        }

        List<Master> masters = masterService.findAllByPublicKeys(new ArrayList<>(addressesSet));
        if (masters.size() != addressesSet.size()) {
            throw new BaseException("Some addresses are not exists in Bevis db!");
        }

        if (masters.stream().anyMatch(master -> !Objects.equals(master.getBlockchain(), blockchain))) {
            throw new BaseException("Some addresses have different blockchain type!");
        }

        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setCode(UUID.randomUUID().toString());
        tokenRequest.setName(request.getName());
        tokenRequest.setStatus(TokenRequestStatus.PENDING);
        tokenRequest.setTokenId(request.getTokenId());
        tokenRequest.setAddressesCount(addressesSet.size());
        tokenRequest.setCreatedAt(Instant.now());
        tokenRequest.setUpdatedAt(Instant.now());
        tokenRequest.setBlockchain(blockchain);

        List<TokenTransfer> tokenTransfers = addressesSet.stream()
                .map(destinationAddress -> {
                    TokenTransfer tokenTransfer = new TokenTransfer();
                    tokenTransfer.setDestinationAddress(destinationAddress);
                    tokenTransfer.setCreatedAt(Instant.now());
                    tokenTransfer.setUpdatedAt(Instant.now());
                    tokenTransfer.setTokenRequest(tokenRequest);
                    return tokenTransfer;
                }).collect(Collectors.toList());

        final TokenRequest newTokenRequest = tokenRequestService.createOrUpdate(tokenRequest);
        tokenTransferService.saveAll(tokenTransfers);

        final Long tokenRequestId = newTokenRequest.getId();

        TokenRequestDetailsDTO tokenRequestDetailsDTO = tokenRequestDetailsService.getDetails(tokenRequestId);

        asyncService.runAfterCommit(() -> tokenRequestProcessingProxyService.processTokenRequest(tokenRequestId));

        return tokenRequestDetailsDTO;
    }

    @Transactional
    @Override
    public TokenRequestDetailsDTO retryFailedTransfers(Long tokenRequestId) {
        TokenRequest tokenRequest = tokenRequestService.findById(tokenRequestId).orElseThrow();

        if (tokenRequest.getStatus() != TokenRequestStatus.FAILED) {
            throw new BaseException("TokenRequest is PENDING or already PROCESSED");
        }

        tokenRequest.setStatus(TokenRequestStatus.PENDING);
        final TokenRequest updatedTokenRequest = tokenRequestService.createOrUpdate(tokenRequest);

        TokenRequestDetailsDTO tokenRequestDetailsDTO = tokenRequestDetailsService.getDetails(updatedTokenRequest.getId());

        asyncService.runAfterCommit(() -> tokenRequestProcessingProxyService.retryFailedTransfers(tokenRequestId));
        return tokenRequestDetailsDTO;
    }

    private boolean isSupportTokenRequest(Blockchain blockchain) {
        return blockchain.getHasTokens() && "SLP".equals(blockchain.getName());
    }
}
