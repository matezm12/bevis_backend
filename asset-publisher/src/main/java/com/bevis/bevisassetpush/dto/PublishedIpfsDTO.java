package com.bevis.bevisassetpush.dto;

import com.bevis.user.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublishedIpfsDTO {
    private String ipfs;
    private String fileExtension;
    private User user;
}
