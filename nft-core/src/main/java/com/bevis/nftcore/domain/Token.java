package com.bevis.nftcore.domain;

import com.bevis.blockchaincore.domain.Blockchain;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Table(name = "token")
public class Token {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "token_id")
    private String tokenId;

    @NotNull
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "blockchain_id")
    private Blockchain blockchain;

    @Column(name = "created_date")
    @CreatedDate
    private Instant createdDate = Instant.now();

    @Column(name = "last_modified_date")
    @LastModifiedDate
    private Instant lastModifiedDate = Instant.now();

    public Token() {
    }

    public Long getId() {
        return this.id;
    }

    public String getTokenId() {
        return this.tokenId;
    }

    public String getName() {
        return this.name;
    }

    public Blockchain getBlockchain() {
        return this.blockchain;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBlockchain(Blockchain blockchain) {
        this.blockchain = blockchain;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Token)) return false;
        final Token other = (Token) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$tokenId = this.getTokenId();
        final Object other$tokenId = other.getTokenId();
        if (this$tokenId == null ? other$tokenId != null : !this$tokenId.equals(other$tokenId)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$blockchain = this.getBlockchain();
        final Object other$blockchain = other.getBlockchain();
        if (this$blockchain == null ? other$blockchain != null : !this$blockchain.equals(other$blockchain))
            return false;
        final Object this$createdDate = this.getCreatedDate();
        final Object other$createdDate = other.getCreatedDate();
        if (this$createdDate == null ? other$createdDate != null : !this$createdDate.equals(other$createdDate))
            return false;
        final Object this$lastModifiedDate = this.getLastModifiedDate();
        final Object other$lastModifiedDate = other.getLastModifiedDate();
        if (this$lastModifiedDate == null ? other$lastModifiedDate != null : !this$lastModifiedDate.equals(other$lastModifiedDate))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Token;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $tokenId = this.getTokenId();
        result = result * PRIME + ($tokenId == null ? 43 : $tokenId.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $blockchain = this.getBlockchain();
        result = result * PRIME + ($blockchain == null ? 43 : $blockchain.hashCode());
        final Object $createdDate = this.getCreatedDate();
        result = result * PRIME + ($createdDate == null ? 43 : $createdDate.hashCode());
        final Object $lastModifiedDate = this.getLastModifiedDate();
        result = result * PRIME + ($lastModifiedDate == null ? 43 : $lastModifiedDate.hashCode());
        return result;
    }

    public String toString() {
        return "Token(id=" + this.getId() + ", tokenId=" + this.getTokenId() +
                ", name=" + this.getName() +
                ", blockchain=" + this.getBlockchain() +
                ", createdDate=" + this.getCreatedDate() +
                ", lastModifiedDate=" + this.getLastModifiedDate() + ")";
    }
}
