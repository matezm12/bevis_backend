package com.bevis.appbe.web.rest.common;

import com.bevis.appbe.web.rest.vm.VersionVm;
import com.bevis.versioning.VersionChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class VersionConfigController {

    private final VersionChecker versionChecker;

    @GetMapping("/version-name")
    VersionVm getVersionName() {
        log.debug("getVersionName");
        return VersionVm.of(versionChecker.getVersionName());
    }
}
