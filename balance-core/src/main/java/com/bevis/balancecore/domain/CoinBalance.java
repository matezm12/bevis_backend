package com.bevis.balancecore.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Table(name = "coin_balance")
@Entity
public class CoinBalance {

    @Id
    @Column(name = "public_key")
    private String publicKey;

    @Column(name = "currency")
    private String currency;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "divider")
    private Double divider;

    @Column(name = "last_scanned")
    @CreatedDate
    private Instant lastScanned;

    @Column(name = "created_at")
    @CreatedDate
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    @LastModifiedDate
    private Instant updatedAt = Instant.now();

    public CoinBalance() {
    }

    public String getPublicKey() {
        return this.publicKey;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Double getBalance() {
        return this.balance;
    }

    public Double getDivider() {
        return this.divider;
    }

    public Instant getLastScanned() {
        return this.lastScanned;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setDivider(Double divider) {
        this.divider = divider;
    }

    public void setLastScanned(Instant lastScanned) {
        this.lastScanned = lastScanned;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof CoinBalance)) return false;
        final CoinBalance other = (CoinBalance) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$publicKey = this.getPublicKey();
        final Object other$publicKey = other.getPublicKey();
        if (this$publicKey == null ? other$publicKey != null : !this$publicKey.equals(other$publicKey)) return false;
        final Object this$currency = this.getCurrency();
        final Object other$currency = other.getCurrency();
        if (this$currency == null ? other$currency != null : !this$currency.equals(other$currency)) return false;
        final Object this$balance = this.getBalance();
        final Object other$balance = other.getBalance();
        if (this$balance == null ? other$balance != null : !this$balance.equals(other$balance)) return false;
        final Object this$divider = this.getDivider();
        final Object other$divider = other.getDivider();
        if (this$divider == null ? other$divider != null : !this$divider.equals(other$divider)) return false;
        final Object this$lastScanned = this.getLastScanned();
        final Object other$lastScanned = other.getLastScanned();
        if (this$lastScanned == null ? other$lastScanned != null : !this$lastScanned.equals(other$lastScanned))
            return false;
        final Object this$createdAt = this.getCreatedAt();
        final Object other$createdAt = other.getCreatedAt();
        if (this$createdAt == null ? other$createdAt != null : !this$createdAt.equals(other$createdAt)) return false;
        final Object this$updatedAt = this.getUpdatedAt();
        final Object other$updatedAt = other.getUpdatedAt();
        if (this$updatedAt == null ? other$updatedAt != null : !this$updatedAt.equals(other$updatedAt)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CoinBalance;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $publicKey = this.getPublicKey();
        result = result * PRIME + ($publicKey == null ? 43 : $publicKey.hashCode());
        final Object $currency = this.getCurrency();
        result = result * PRIME + ($currency == null ? 43 : $currency.hashCode());
        final Object $balance = this.getBalance();
        result = result * PRIME + ($balance == null ? 43 : $balance.hashCode());
        final Object $divider = this.getDivider();
        result = result * PRIME + ($divider == null ? 43 : $divider.hashCode());
        final Object $lastScanned = this.getLastScanned();
        result = result * PRIME + ($lastScanned == null ? 43 : $lastScanned.hashCode());
        final Object $createdAt = this.getCreatedAt();
        result = result * PRIME + ($createdAt == null ? 43 : $createdAt.hashCode());
        final Object $updatedAt = this.getUpdatedAt();
        result = result * PRIME + ($updatedAt == null ? 43 : $updatedAt.hashCode());
        return result;
    }

    public String toString() {
        return "CoinBalance(publicKey=" + this.getPublicKey() + ", currency=" + this.getCurrency() + ", balance=" + this.getBalance() + ", divider=" + this.getDivider() + ", lastScanned=" + this.getLastScanned() + ", createdAt=" + this.getCreatedAt() + ", updatedAt=" + this.getUpdatedAt() + ")";
    }
}
