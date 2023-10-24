package com.bevis.events;

public interface EventPublishingService {
    void publishEvent(Object o);
    void publishEventAsync(Object o);
}
