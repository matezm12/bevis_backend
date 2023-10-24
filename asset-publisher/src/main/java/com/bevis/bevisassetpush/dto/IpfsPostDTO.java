package com.bevis.bevisassetpush.dto;

import com.bevis.master.domain.Master;
import com.bevis.user.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IpfsPostDTO {
    private String ipfs;
    private Master master;
    private User currentUser;
    private String fileExtension;
}
