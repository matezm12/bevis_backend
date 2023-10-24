package com.bevis.assetinfo;

import com.bevis.master.domain.Master;

public interface LogoFetchingService {
    String loadFileUrlByMaster(Master master);
}
