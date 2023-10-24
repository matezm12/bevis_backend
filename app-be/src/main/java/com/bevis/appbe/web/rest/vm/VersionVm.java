package com.bevis.appbe.web.rest.vm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class VersionVm {
    private String versionName;
}
