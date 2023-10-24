package com.bevis.bevisassetpush.dto;

import com.bevis.master.domain.Master;
import com.bevis.user.domain.User;
import com.bevis.files.dto.File;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FilePostDTO {
    private File tempFile;
    private Master master;
    private FileParametersDTO fileParametersDTO;
    private User currentUser;
    //Set true if encrypted
    private Boolean encrypted;
}
