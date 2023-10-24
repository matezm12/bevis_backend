package com.bevis.balancecore.domain;

import javax.persistence.*;

@Table(name = "coin_balance_source")
@Entity
public class CoinBalanceSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency")
    private String currency;

    /**
     * "sourceKey" - name of source, suffic in AWS API Gateway route.
     */
    @Column(name = "source_key")
    private String sourceKey;

    /**
     * "Multi" - this key show if this source supports requests for
     * loading of multiple balance addresses in single request.
     */
    @Column(name = "multi")
    private boolean multi;

    /**
     * "multiPost" - type of the request, POST if "true" and GET - if false
     */
    @Column(name = "multi_post")
    private Boolean multiPost;

    /**
     * "multiLimit" (if source supports loading multiple balances in single
     * request), this is the maximum amount of addresses which can be loaded in
     * this request
     */
    @Column(name = "multi_limit")
    private Integer multiLimit;

    /**
     * "Live" - flag value show, if source is "live" or "dead"
     */
    @Column(name = "live")
    private boolean live;

    @Column(name = "description")
    private String description;

    public CoinBalanceSource() {
    }

    public Long getId() {
        return this.id;
    }

    public String getCurrency() {
        return this.currency;
    }

    public String getSourceKey() {
        return this.sourceKey;
    }

    public boolean isMulti() {
        return this.multi;
    }

    public Boolean getMultiPost() {
        return this.multiPost;
    }

    public Integer getMultiLimit() {
        return this.multiLimit;
    }

    public boolean isLive() {
        return this.live;
    }

    public String getDescription() {
        return this.description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    public void setMulti(boolean multi) {
        this.multi = multi;
    }

    public void setMultiPost(Boolean multiPost) {
        this.multiPost = multiPost;
    }

    public void setMultiLimit(Integer multiLimit) {
        this.multiLimit = multiLimit;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof CoinBalanceSource)) return false;
        final CoinBalanceSource other = (CoinBalanceSource) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$currency = this.getCurrency();
        final Object other$currency = other.getCurrency();
        if (this$currency == null ? other$currency != null : !this$currency.equals(other$currency)) return false;
        final Object this$sourceKey = this.getSourceKey();
        final Object other$sourceKey = other.getSourceKey();
        if (this$sourceKey == null ? other$sourceKey != null : !this$sourceKey.equals(other$sourceKey)) return false;
        if (this.isMulti() != other.isMulti()) return false;
        final Object this$multiPost = this.getMultiPost();
        final Object other$multiPost = other.getMultiPost();
        if (this$multiPost == null ? other$multiPost != null : !this$multiPost.equals(other$multiPost)) return false;
        final Object this$multiLimit = this.getMultiLimit();
        final Object other$multiLimit = other.getMultiLimit();
        if (this$multiLimit == null ? other$multiLimit != null : !this$multiLimit.equals(other$multiLimit))
            return false;
        if (this.isLive() != other.isLive()) return false;
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CoinBalanceSource;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $currency = this.getCurrency();
        result = result * PRIME + ($currency == null ? 43 : $currency.hashCode());
        final Object $sourceKey = this.getSourceKey();
        result = result * PRIME + ($sourceKey == null ? 43 : $sourceKey.hashCode());
        result = result * PRIME + (this.isMulti() ? 79 : 97);
        final Object $multiPost = this.getMultiPost();
        result = result * PRIME + ($multiPost == null ? 43 : $multiPost.hashCode());
        final Object $multiLimit = this.getMultiLimit();
        result = result * PRIME + ($multiLimit == null ? 43 : $multiLimit.hashCode());
        result = result * PRIME + (this.isLive() ? 79 : 97);
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        return result;
    }

    public String toString() {
        return "CoinBalanceSource(id=" + this.getId() + ", currency=" + this.getCurrency() + ", sourceKey=" + this.getSourceKey() + ", multi=" + this.isMulti() + ", multiPost=" + this.getMultiPost() + ", multiLimit=" + this.getMultiLimit() + ", live=" + this.isLive() + ", description=" + this.getDescription() + ")";
    }
}
