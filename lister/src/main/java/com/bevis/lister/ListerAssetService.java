package com.bevis.lister;

import com.bevis.events.dto.user.UserDataDeleteEvent;
import com.bevis.lister.dto.ListerAssetRequest;
import com.bevis.lister.dto.ListerAssetResponse;
import com.bevis.user.domain.User;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ListerAssetService {
    ListerAssetResponse findOne(String assetId, User user);
    Page<ListerAssetResponse> getUserList(User user, Pageable pageable, String displayCurrency);
    Page<ListerAssetResponse> getUserList(User user, String search, Pageable pageable, String displayCurrency);
    ListerAssetResponse addToUserList(ListerAssetRequest assetRequest, User user);
    void removeFromUserList(String assetId, User user);
    ListerAssetResponse renameForUser(String assetId, String newName, User user);

    @EventListener
    void handleUserDataDeleteEvent(UserDataDeleteEvent e);
}
