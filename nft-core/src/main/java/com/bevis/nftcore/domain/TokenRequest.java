package com.bevis.nftcore.domain;

import com.bevis.blockchaincore.domain.Blockchain;
import com.bevis.nftcore.domain.enumeration.TokenRequestStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "token_request")
public class TokenRequest {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_code")
    private String code;

    @Column(name = "request_name")
    private String name;

    @Column(name = "request_status")
    @Enumerated(EnumType.STRING)
    private TokenRequestStatus status;

    @Column(name = "token_id")
    private String tokenId;

    @Column(name = "addr_count")
    private Integer addressesCount;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "blockchain_id")
    private Blockchain blockchain;

    @JsonIgnore
    @OneToMany(mappedBy = "tokenRequest", fetch = FetchType.LAZY)
    private Set<TokenTransfer> tokenTransfers = new HashSet<>();

    public TokenRequest() {
    }


    public Long getId() {
        return this.id;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public TokenRequestStatus getStatus() {
        return this.status;
    }

    public String getTokenId() {
        return this.tokenId;
    }

    public Integer getAddressesCount() {
        return this.addressesCount;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Blockchain getBlockchain() {
        return this.blockchain;
    }

    public Set<TokenTransfer> getTokenTransfers() {
        return this.tokenTransfers;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(TokenRequestStatus status) {
        this.status = status;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public void setAddressesCount(Integer addressesCount) {
        this.addressesCount = addressesCount;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setBlockchain(Blockchain blockchain) {
        this.blockchain = blockchain;
    }

    @JsonIgnore
    public void setTokenTransfers(Set<TokenTransfer> tokenTransfers) {
        this.tokenTransfers = tokenTransfers;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof TokenRequest)) return false;
        final TokenRequest other = (TokenRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$code = this.getCode();
        final Object other$code = other.getCode();
        if (this$code == null ? other$code != null : !this$code.equals(other$code)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
        final Object this$tokenId = this.getTokenId();
        final Object other$tokenId = other.getTokenId();
        if (this$tokenId == null ? other$tokenId != null : !this$tokenId.equals(other$tokenId)) return false;
        final Object this$addressesCount = this.getAddressesCount();
        final Object other$addressesCount = other.getAddressesCount();
        if (this$addressesCount == null ? other$addressesCount != null : !this$addressesCount.equals(other$addressesCount))
            return false;
        final Object this$createdAt = this.getCreatedAt();
        final Object other$createdAt = other.getCreatedAt();
        if (this$createdAt == null ? other$createdAt != null : !this$createdAt.equals(other$createdAt)) return false;
        final Object this$updatedAt = this.getUpdatedAt();
        final Object other$updatedAt = other.getUpdatedAt();
        if (this$updatedAt == null ? other$updatedAt != null : !this$updatedAt.equals(other$updatedAt)) return false;
        final Object this$blockchain = this.getBlockchain();
        final Object other$blockchain = other.getBlockchain();
        if (this$blockchain == null ? other$blockchain != null : !this$blockchain.equals(other$blockchain))
            return false;
        final Object this$tokenTransfers = this.getTokenTransfers();
        final Object other$tokenTransfers = other.getTokenTransfers();
        if (this$tokenTransfers == null ? other$tokenTransfers != null : !this$tokenTransfers.equals(other$tokenTransfers))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof TokenRequest;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $code = this.getCode();
        result = result * PRIME + ($code == null ? 43 : $code.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $status = this.getStatus();
        result = result * PRIME + ($status == null ? 43 : $status.hashCode());
        final Object $tokenId = this.getTokenId();
        result = result * PRIME + ($tokenId == null ? 43 : $tokenId.hashCode());
        final Object $addressesCount = this.getAddressesCount();
        result = result * PRIME + ($addressesCount == null ? 43 : $addressesCount.hashCode());
        final Object $createdAt = this.getCreatedAt();
        result = result * PRIME + ($createdAt == null ? 43 : $createdAt.hashCode());
        final Object $updatedAt = this.getUpdatedAt();
        result = result * PRIME + ($updatedAt == null ? 43 : $updatedAt.hashCode());
        final Object $blockchain = this.getBlockchain();
        result = result * PRIME + ($blockchain == null ? 43 : $blockchain.hashCode());
        final Object $tokenTransfers = this.getTokenTransfers();
        result = result * PRIME + ($tokenTransfers == null ? 43 : $tokenTransfers.hashCode());
        return result;
    }

    public String toString() {
        return "TokenRequest(id=" + this.getId() + ", code=" + this.getCode() + ", name=" + this.getName() + ", status=" + this.getStatus() + ", tokenId=" + this.getTokenId() + ", addressesCount=" + this.getAddressesCount() + ", createdAt=" + this.getCreatedAt() + ", updatedAt=" + this.getUpdatedAt() + ", blockchain=" + this.getBlockchain() + ", tokenTransfers=" + this.getTokenTransfers() + ")";
    }
}
