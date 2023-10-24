package com.bevis.keygen;

public class CryptoKeyDTO {

    private String wif;

    private String address;

    public CryptoKeyDTO() {
    }

    public String getWif() {
        return this.wif;
    }

    public String getAddress() {
        return this.address;
    }

    public void setWif(String wif) {
        this.wif = wif;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof CryptoKeyDTO)) return false;
        final CryptoKeyDTO other = (CryptoKeyDTO) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$wif = this.getWif();
        final Object other$wif = other.getWif();
        if (this$wif == null ? other$wif != null : !this$wif.equals(other$wif)) return false;
        final Object this$address = this.getAddress();
        final Object other$address = other.getAddress();
        if (this$address == null ? other$address != null : !this$address.equals(other$address)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CryptoKeyDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $wif = this.getWif();
        result = result * PRIME + ($wif == null ? 43 : $wif.hashCode());
        final Object $address = this.getAddress();
        result = result * PRIME + ($address == null ? 43 : $address.hashCode());
        return result;
    }

    public String toString() {
        return "CryptoKeyDTO(wif=" + this.getWif() + ", address=" + this.getAddress() + ")";
    }
}
