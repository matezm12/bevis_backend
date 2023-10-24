package com.bevis.credits;

import com.bevis.credits.domain.CreditsPackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CreditsPackagesManagementService {

    Page<CreditsPackage> findAll(Pageable pageable);

}
