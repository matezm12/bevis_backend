package com.bevis.nftcore.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "nft_token")
public class NftToken {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "token_chain")
    private String chain;

    @Column(name = "token_id")
    private String tokenId;

    @Column(name = "token_symbol")
    private String symbol;

    @Column(name = "token_address")
    private String address;

    @Column(name = "token_name")
    private String name;

    @Column(name = "token_description")
    private String description;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "collection")
    private String collection;

    @Column(name = "market_url")
    private String marketUrl;

    @Column(name = "blockchain_url")
    private String blockchainUrl;

    public NftToken() {
    }

    public String getId() {
        return this.id;
    }

    public String getChain() {
        return this.chain;
    }

    public String getTokenId() {
        return this.tokenId;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public String getAddress() {
        return this.address;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public String getCollection() {
        return this.collection;
    }

    public String getMarketUrl() {
        return this.marketUrl;
    }

    public String getBlockchainUrl() {
        return this.blockchainUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public void setMarketUrl(String marketUrl) {
        this.marketUrl = marketUrl;
    }

    public void setBlockchainUrl(String blockchainUrl) {
        this.blockchainUrl = blockchainUrl;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof NftToken)) return false;
        final NftToken other = (NftToken) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$chain = this.getChain();
        final Object other$chain = other.getChain();
        if (this$chain == null ? other$chain != null : !this$chain.equals(other$chain)) return false;
        final Object this$tokenId = this.getTokenId();
        final Object other$tokenId = other.getTokenId();
        if (this$tokenId == null ? other$tokenId != null : !this$tokenId.equals(other$tokenId)) return false;
        final Object this$symbol = this.getSymbol();
        final Object other$symbol = other.getSymbol();
        if (this$symbol == null ? other$symbol != null : !this$symbol.equals(other$symbol)) return false;
        final Object this$address = this.getAddress();
        final Object other$address = other.getAddress();
        if (this$address == null ? other$address != null : !this$address.equals(other$address)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description))
            return false;
        final Object this$fileUrl = this.getFileUrl();
        final Object other$fileUrl = other.getFileUrl();
        if (this$fileUrl == null ? other$fileUrl != null : !this$fileUrl.equals(other$fileUrl)) return false;
        final Object this$collection = this.getCollection();
        final Object other$collection = other.getCollection();
        if (this$collection == null ? other$collection != null : !this$collection.equals(other$collection))
            return false;
        final Object this$marketUrl = this.getMarketUrl();
        final Object other$marketUrl = other.getMarketUrl();
        if (this$marketUrl == null ? other$marketUrl != null : !this$marketUrl.equals(other$marketUrl)) return false;
        final Object this$blockchainUrl = this.getBlockchainUrl();
        final Object other$blockchainUrl = other.getBlockchainUrl();
        if (this$blockchainUrl == null ? other$blockchainUrl != null : !this$blockchainUrl.equals(other$blockchainUrl))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof NftToken;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $chain = this.getChain();
        result = result * PRIME + ($chain == null ? 43 : $chain.hashCode());
        final Object $tokenId = this.getTokenId();
        result = result * PRIME + ($tokenId == null ? 43 : $tokenId.hashCode());
        final Object $symbol = this.getSymbol();
        result = result * PRIME + ($symbol == null ? 43 : $symbol.hashCode());
        final Object $address = this.getAddress();
        result = result * PRIME + ($address == null ? 43 : $address.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        final Object $fileUrl = this.getFileUrl();
        result = result * PRIME + ($fileUrl == null ? 43 : $fileUrl.hashCode());
        final Object $collection = this.getCollection();
        result = result * PRIME + ($collection == null ? 43 : $collection.hashCode());
        final Object $marketUrl = this.getMarketUrl();
        result = result * PRIME + ($marketUrl == null ? 43 : $marketUrl.hashCode());
        final Object $blockchainUrl = this.getBlockchainUrl();
        result = result * PRIME + ($blockchainUrl == null ? 43 : $blockchainUrl.hashCode());
        return result;
    }

    public String toString() {
        return "NftToken(id=" + this.getId() + ", chain=" + this.getChain() + ", tokenId=" + this.getTokenId() + ", symbol=" + this.getSymbol() + ", address=" + this.getAddress() + ", name=" + this.getName() + ", description=" + this.getDescription() + ", fileUrl=" + this.getFileUrl() + ", collection=" + this.getCollection() + ", marketUrl=" + this.getMarketUrl() + ", blockchainUrl=" + this.getBlockchainUrl() + ")";
    }
}
