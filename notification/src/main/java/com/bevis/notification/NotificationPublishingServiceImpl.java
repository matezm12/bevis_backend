package com.bevis.notification;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.AmazonSNSException;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.bevis.notification.dto.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
class NotificationPublishingServiceImpl implements NotificationPublishingService {

    private final NotificationProps notificationProps;
    private final AmazonSNS amazonSNS;

    @Override
    public void publishGeneralEvent(Event event) throws NotificationException {
        publish(event, notificationProps.getSns().getGeneralTopic());
    }

    @Override
    public void publishErrorEvent(Event event) throws NotificationException {
        publish(event, notificationProps.getSns().getServiceErrorTopic());
    }

    @Override
    public void publishUserActionEvent(Event event) throws NotificationException {
        publish(event, notificationProps.getSns().getUserActionTopic());
    }

    private void publish(Event event, String topicArn) throws NotificationException {
        try {
            PublishResult publishResult = amazonSNS.publish(new PublishRequest()
                    .withSubject(event.getSubject())
                    .withMessage(event.getMessage())
                    .withTopicArn(topicArn));
            log.info(publishResult.getMessageId() + " Message sent. Status is " +
                    publishResult.getSdkHttpMetadata().getHttpStatusCode());
        } catch (AmazonSNSException e) {
            throw new NotificationException("Error sending message to SNS. " + e.getMessage(), e);
        }
    }
}
