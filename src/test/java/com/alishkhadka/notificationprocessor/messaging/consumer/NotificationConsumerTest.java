package com.alishkhadka.notificationprocessor.messaging.consumer;

import com.alishkhadka.notificationprocessor.constant.NotificationType;
import com.alishkhadka.notificationprocessor.messaging.dto.NotificationMessageDto;
import com.alishkhadka.notificationprocessor.service.NotificationProcessorFactory;
import com.alishkhadka.notificationprocessor.service.NotificationProcessorService;
import com.alishkhadka.notificationprocessor.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationConsumerTest {

    @Mock
    NotificationProcessorService mockNotificationProcessorService;
    @Mock
    NotificationProcessorFactory mockNotificationProcessorFactory;

    @InjectMocks
    NotificationConsumer notificationConsumer;

    @Test
    void shouldConsumePushNotification() {
        NotificationMessageDto dto = new NotificationMessageDto();
        dto.setUserId(1L);
        dto.setMessageType("PUSH_NOTIFICATION");
        dto.setContent("Test Message");

        String json = JsonUtils.toJson(dto);

        when(mockNotificationProcessorFactory.getProcessorService(NotificationType.PUSH_NOTIFICATION))
                .thenReturn(Optional.of(mockNotificationProcessorService));
        when(mockNotificationProcessorService.process(any(NotificationMessageDto.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(notificationConsumer.consumeEvent(json))
                .verifyComplete();
    }

    @Test
    void shouldErrorConsumingRandomMessageType() {
        NotificationMessageDto dto = new NotificationMessageDto();
        dto.setUserId(1L);
        dto.setMessageType("RANDOM_NOTIFICATION");
        dto.setContent("Test Error Message Type");

        String json = JsonUtils.toJson(dto);

        when(mockNotificationProcessorFactory.getProcessorService(NotificationType.NONE))
                .thenReturn(Optional.empty());

        StepVerifier.create(notificationConsumer.consumeEvent(json))
                .expectComplete()
                .verify();
    }
}