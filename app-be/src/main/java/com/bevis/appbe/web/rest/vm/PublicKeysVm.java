package com.bevis.appbe.web.rest.vm;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PublicKeysVm {

    @NotNull
    @Valid
    private List<String> publicKeys;
}
