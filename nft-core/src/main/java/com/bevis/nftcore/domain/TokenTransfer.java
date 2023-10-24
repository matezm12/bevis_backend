package com.bevis.nftcore.domain;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "token_transfer")
public class TokenTransfer {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dest_address")
    private String destinationAddress;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private TokenRequest tokenRequest;

    public TokenTransfer() {
    }

    public Long getId() {
        return this.id;
    }

    public String getDestinationAddress() {
        return this.destinationAddress;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public TokenRequest getTokenRequest() {
        return this.tokenRequest;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setTokenRequest(TokenRequest tokenRequest) {
        this.tokenRequest = tokenRequest;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof TokenTransfer)) return false;
        final TokenTransfer other = (TokenTransfer) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$destinationAddress = this.getDestinationAddress();
        final Object other$destinationAddress = other.getDestinationAddress();
        if (this$destinationAddress == null ? other$destinationAddress != null : !this$destinationAddress.equals(other$destinationAddress))
            return false;
        final Object this$transactionId = this.getTransactionId();
        final Object other$transactionId = other.getTransactionId();
        if (this$transactionId == null ? other$transactionId != null : !this$transactionId.equals(other$transactionId))
            return false;
        final Object this$errorMessage = this.getErrorMessage();
        final Object other$errorMessage = other.getErrorMessage();
        if (this$errorMessage == null ? other$errorMessage != null : !this$errorMessage.equals(other$errorMessage))
            return false;
        final Object this$createdAt = this.getCreatedAt();
        final Object other$createdAt = other.getCreatedAt();
        if (this$createdAt == null ? other$createdAt != null : !this$createdAt.equals(other$createdAt)) return false;
        final Object this$updatedAt = this.getUpdatedAt();
        final Object other$updatedAt = other.getUpdatedAt();
        if (this$updatedAt == null ? other$updatedAt != null : !this$updatedAt.equals(other$updatedAt)) return false;
        final Object this$tokenRequest = this.getTokenRequest();
        final Object other$tokenRequest = other.getTokenRequest();
        if (this$tokenRequest == null ? other$tokenRequest != null : !this$tokenRequest.equals(other$tokenRequest))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof TokenTransfer;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $destinationAddress = this.getDestinationAddress();
        result = result * PRIME + ($destinationAddress == null ? 43 : $destinationAddress.hashCode());
        final Object $transactionId = this.getTransactionId();
        result = result * PRIME + ($transactionId == null ? 43 : $transactionId.hashCode());
        final Object $errorMessage = this.getErrorMessage();
        result = result * PRIME + ($errorMessage == null ? 43 : $errorMessage.hashCode());
        final Object $createdAt = this.getCreatedAt();
        result = result * PRIME + ($createdAt == null ? 43 : $createdAt.hashCode());
        final Object $updatedAt = this.getUpdatedAt();
        result = result * PRIME + ($updatedAt == null ? 43 : $updatedAt.hashCode());
        final Object $tokenRequest = this.getTokenRequest();
        result = result * PRIME + ($tokenRequest == null ? 43 : $tokenRequest.hashCode());
        return result;
    }

    public String toString() {
        return "TokenTransfer(id=" + this.getId() + ", destinationAddress=" + this.getDestinationAddress() + ", transactionId=" + this.getTransactionId() + ", errorMessage=" + this.getErrorMessage() + ", createdAt=" + this.getCreatedAt() + ", updatedAt=" + this.getUpdatedAt() + ", tokenRequest=" + this.getTokenRequest() + ")";
    }
}
