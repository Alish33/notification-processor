package com.alishkhadka.notificationprocessor.service.usecase;

import com.alishkhadka.notificationprocessor.service.metadata.UserMetaData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalUserMetaDataUseCaseTest {

    @Mock
    private WebClient client;
    @Mock
    private WebClient.Builder webClient;
    @Mock
    WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    WebClient.ResponseSpec responseSpec;

    private ExternalUserMetaDataUseCase service;
    private UserMetaData mockUserMetaData;

    @BeforeEach
    void setup() {
        mockUserMetaData = new UserMetaData();
        mockUserMetaData.setUserId(930504453L);

        when(webClient.build()).thenReturn(client);
        when(client.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);


        service = new ExternalUserMetaDataUseCase(
                webClient,
                "https://mockserver.free.beeceptor.com/api/users/",
                5,
                3,
                2
        );
    }

    @Test
    void shouldReturnUserMetaData() {

        when(responseSpec.bodyToMono(UserMetaData.class)).thenReturn(Mono.just(mockUserMetaData));

        Mono<UserMetaData> metaData = service.getExternalNotificationUserMetaData(mockUserMetaData.getUserId());

        StepVerifier.create(metaData)
                .expectNextMatches(data -> data.getUserId().equals(mockUserMetaData.getUserId()))
                .verifyComplete();

    }

    @Test
    void shouldFail_On4xxError() {
        when(responseSpec.bodyToMono(UserMetaData.class))
                .thenReturn(Mono.error(new IllegalArgumentException("4xx Client error")));

        Mono<UserMetaData> metaData = service.getExternalNotificationUserMetaData(mockUserMetaData.getUserId());

        StepVerifier.create(metaData)
                .expectErrorMessage("4xx Client error")
                .verify();
    }


    @Test
    void shouldFail_On5xxError() {
        when(responseSpec.bodyToMono(UserMetaData.class))
                .thenReturn(Mono.error(new IllegalArgumentException("5xx Client error")));

        Mono<UserMetaData> metaData = service.getExternalNotificationUserMetaData(mockUserMetaData.getUserId());

        StepVerifier.create(metaData)
                .expectErrorMessage("5xx Client error")
                .verify();
    }

    @Test
    void shouldFail_WhenResponseIsDelayedWithRetry() {

        when(responseSpec.bodyToMono(UserMetaData.class))
                .thenReturn(Mono.delay(Duration.ofSeconds(6))
                        .then(Mono.just(new UserMetaData())));

        Mono<UserMetaData> result = service.getExternalNotificationUserMetaData(mockUserMetaData.getUserId());

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().contains("Retries exhausted"))
                .verify();
    }

}

