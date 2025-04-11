package com.alishkhadka.notificationprocessor.service;

import com.alishkhadka.notificationprocessor.constant.NotificationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NotificationProcessorFactory {

    private final Logger logger = LoggerFactory.getLogger(NotificationProcessorFactory.class);
    private final ApplicationContext applicationContext;

    @Autowired
    public NotificationProcessorFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Optional<NotificationProcessorService> getProcessorService(NotificationType notificationType) {
        var services = applicationContext
                .getBeansOfType(NotificationProcessorService.class)
                .values()
                .toArray(new NotificationProcessorService[0]);

        for (var processorService : services) {
            if (notificationType.getValue().equals(processorService.messageType())) {
                logger.info("Found notification processor for :: {}", notificationType.getValue());
                return Optional.of(processorService);
            }
        }
        return Optional.empty();
    }
}
