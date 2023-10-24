package com.bevis.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventPublishingServiceImpl implements EventPublishingService {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishEvent(final Object o) {
        log.debug("Publishing event. ");
        applicationEventPublisher.publishEvent(o);
    }

    @Async
    @Override
    public void publishEventAsync(Object o) {
        publishEvent(o);
    }
}
