package com.bevis.notification;

import com.bevis.events.dto.ErrorEvent;
import com.bevis.events.dto.GeneralEvent;
import com.bevis.events.dto.UserActionEvent;
import com.bevis.notification.dto.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class NotificationListenerService {

    private final NotificationPublishingService notificationPublishingService;

    @EventListener
    public void handleGeneralEvent(GeneralEvent event) {
        try {
            notificationPublishingService.publishGeneralEvent(Event.builder()
                    .subject(event.getSubject())
                    .message(event.getMessage())
                    .build());
        } catch (NotificationException e) {
            log.error(e.getMessage());
        }
    }

    @EventListener
    public void handleUserActionEvent(UserActionEvent event) {
        try {
            notificationPublishingService.publishUserActionEvent(Event.builder()
                    .subject(event.getAction())
                    .message(event.getUser() + "\n" + event.getDetails() + "\n" + event.getLink())
                    .build());
        } catch (NotificationException e) {
            log.error(e.getMessage());
        }
    }

    @EventListener
    public void handleErrorEvent(ErrorEvent event) {
        try {
            notificationPublishingService.publishErrorEvent(Event.builder()
                    .subject(event.getServiceName())
                    .message(event.getMessage())
                    .build());
        } catch (NotificationException e) {
            log.error(e.getMessage());
        }
    }
}
