package com.alishkhadka.notificationprocessor.service;

import com.alishkhadka.notificationprocessor.messaging.dto.NotificationMessageDto;
import reactor.core.publisher.Mono;

public interface NotificationProcessorService {
    Mono<Void> process(NotificationMessageDto message);
    String messageType();
}
