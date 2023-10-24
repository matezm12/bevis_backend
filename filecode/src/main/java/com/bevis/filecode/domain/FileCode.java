package com.bevis.filecode.domain;

import com.bevis.filecode.domain.enumeration.FileCodeGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@Table(name = "file_codes")
@EqualsAndHashCode(of = "fileType")
public class FileCode {

    @Id
    @Column(name = "file_type")
    private String fileType;

    @Column(name = "priority_code")
    private String priorityCode;

    @Column(name = "placeholder_ipfs")
    private String placeholder;

    @Column(name = "encrypted_ipfs")
    private String encryptedPlaceholder;

    @Column(name = "file_group")
    @Enumerated(EnumType.STRING)
    private FileCodeGroup fileGroup;

}
