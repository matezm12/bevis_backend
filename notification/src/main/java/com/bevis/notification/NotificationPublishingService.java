package com.bevis.notification;

import com.bevis.notification.dto.Event;

public interface NotificationPublishingService {
    void publishGeneralEvent(Event event) throws NotificationException;
    void publishErrorEvent(Event event) throws NotificationException;
    void publishUserActionEvent(Event event) throws NotificationException;
}
