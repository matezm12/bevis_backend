package com.bevis.nftcore.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public class NftMintRequest {
    @NotNull
    private String tokenName;

    @NotNull
    private List<String> receivers;

    @NotNull
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String image;

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<String> receivers) {
        this.receivers = receivers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
