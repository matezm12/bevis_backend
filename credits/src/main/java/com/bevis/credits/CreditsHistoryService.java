package com.bevis.credits;

import com.bevis.user.domain.User;
import com.bevis.credits.dto.CreditsHistoryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CreditsHistoryService {
    Page<CreditsHistoryItem> getUserBalanceHistory(User user, Pageable pageable);
}
