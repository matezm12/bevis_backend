package com.bevis.nftcore.tokenrequest.specification;

import com.bevis.nftcore.domain.TokenRequest;
import com.bevis.nftcore.domain.TokenTransfer;
import com.bevis.nftcore.domain.enumeration.TokenRequestStatus;
import com.bevis.nftcore.tokenrequest.dto.TokenTransferFilterDTO;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.Objects;

public final class TokenTransferSearchSpecification {
    public static Specification<TokenTransfer> byFilterRequest(TokenTransferFilterDTO filter) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Path<TokenRequest> tokenRequestPath = root.join("tokenRequest");
            Predicate mainPredicate = criteriaBuilder.equal(tokenRequestPath.get("id"), filter.getRequestId());
            if (Objects.isNull(filter.getFilterState())) {
                return mainPredicate;
            }
            Predicate processedTransfer = criteriaBuilder.isNotNull(root.get("transactionId"));
            Predicate failedTransfer = criteriaBuilder.isNotNull(root.get("errorMessage"));
            if (filter.getFilterState() == TokenRequestStatus.PROCESSED) {
                return criteriaBuilder.and(mainPredicate, processedTransfer);
            } else if (filter.getFilterState() == TokenRequestStatus.FAILED) {
                return criteriaBuilder.and(mainPredicate, failedTransfer);
            } else {
                Predicate pendingTransfer = criteriaBuilder.not(criteriaBuilder.or(processedTransfer, failedTransfer));
                return criteriaBuilder.and(mainPredicate, pendingTransfer);
            }
        };
    }
}
