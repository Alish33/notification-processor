package com.alishkhadka.notificationprocessor.messaging.consumer;

import com.alishkhadka.notificationprocessor.constant.MessagingConstant;
import com.alishkhadka.notificationprocessor.messaging.dto.NotificationMessageDto;
import com.alishkhadka.notificationprocessor.service.NotificationProcessorFactory;
import com.alishkhadka.notificationprocessor.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.alishkhadka.notificationprocessor.constant.NotificationType.getNotificationType;

@Component
public class NotificationConsumer {

    private final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);
    private final NotificationProcessorFactory processorFactory;

    @Autowired
    public NotificationConsumer(NotificationProcessorFactory processorFactory) {
        this.processorFactory = processorFactory;
    }

    @KafkaListener(
            groupId = MessagingConstant.KAFKA_CONFIG_NOTIFICATION_CONSUMER_GROUP,
            topics = MessagingConstant.KAFKA_NOTIFICATION_EVENT_TOPIC
    )
    public Mono<Void> consumeEvent(String emailNotification) {
        logger.info("Raw consumer notification message : {}", emailNotification);
        var message = JsonUtils.fromJson(emailNotification, NotificationMessageDto.class);
        logger.info("Processed Consumer Message : {}", message);

        return Mono.justOrEmpty(processorFactory.getProcessorService(getNotificationType(message.getMessageType())))
                .switchIfEmpty(Mono.fromRunnable(() ->
                        logger.warn("No processor found for notification type: {}", message.getMessageType())
                ))
                .flatMap(processor -> processor.process(message))
                .doOnError(err -> logger.error("Error processing email notification: {}", err.getMessage()))
                .then();
    }

}
