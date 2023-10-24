package com.bevis.master;

import com.bevis.master.domain.Master;

import java.time.Instant;
import java.util.List;

public interface MasterGeneratingService {
    List<Master> generateMasters(List<String> publicKeys, String cryptoCurrency, Instant scanTime);
}
