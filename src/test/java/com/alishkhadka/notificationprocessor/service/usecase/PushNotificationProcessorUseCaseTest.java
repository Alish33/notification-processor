package com.alishkhadka.notificationprocessor.service.usecase;

import com.alishkhadka.notificationprocessor.messaging.dto.NotificationMessageDto;
import com.alishkhadka.notificationprocessor.repository.entity.User;
import com.alishkhadka.notificationprocessor.service.metadata.UserMetaData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PushNotificationProcessorUseCaseTest {

    @Mock
    ExternalUserMetaDataUseCase externalUserMetaDataUseCaseMock;
    @Mock
    UserUseCase userUseCaseMock;

    @InjectMocks
    private PushNotificationProcessorUseCase processor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        processor = new PushNotificationProcessorUseCase(externalUserMetaDataUseCaseMock, userUseCaseMock);
    }


    @Test
    void shouldProcess_OnValidInput() {
        NotificationMessageDto message = new NotificationMessageDto();
        message.setUserId(123L);

        UserMetaData mockMetaData = new UserMetaData();
        User processedUser = new User();

        when(externalUserMetaDataUseCaseMock.getExternalNotificationUserMetaData(123L)).thenReturn(Mono.just(mockMetaData));
        when(userUseCaseMock.saveUser(any())).thenReturn(Mono.just(processedUser));

        StepVerifier.create(processor.process(message))
                .verifyComplete();

        verify(externalUserMetaDataUseCaseMock).getExternalNotificationUserMetaData(123L);
        verify(userUseCaseMock).saveUser(any());
    }

    @Test
    void shouldError_OnInvalidInput() {
        NotificationMessageDto message = new NotificationMessageDto();

        StepVerifier.create(processor.process(message))
                .expectError(IllegalArgumentException.class)
                .verify();

        verifyNoInteractions(externalUserMetaDataUseCaseMock);
        verifyNoInteractions(userUseCaseMock);
    }



}