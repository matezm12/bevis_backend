package com.bevis.bevisassetpush.dto;

import com.bevis.master.domain.Master;
import com.bevis.user.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceCalculatingData {
    private User user;
    private FileParametersDTO fileParameters;
    private Master master;

    public String getAssetId() {
        return master.getId();
    }
}
