package com.alishkhadka.notificationprocessor.service.usecase;

import com.alishkhadka.notificationprocessor.service.metadata.UserMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

@Service
public class ExternalUserMetaDataUseCase {
    private final Logger logger = LoggerFactory.getLogger(ExternalUserMetaDataUseCase.class);

    private final WebClient webClient;
    private final String userMetaDatEndpoint;
    private final int timeout;
    private final int retryAttempts;
    private final int retryDelay;

    @Autowired
    public ExternalUserMetaDataUseCase(
            WebClient.Builder webClientBuilder,
            @Value("${external.user-metadata-api.endpoint}") String userMetaDatEndpoint,
            @Value("${external.user-metadata-api.timeout}") int timeout,
            @Value("${external.user-metadata-api.retry-attempts}") int retryAttempts,
            @Value("${external.user-metadata-api.retry-delay}") int retryDelay) {
        this.webClient = webClientBuilder.build();
        this.userMetaDatEndpoint = userMetaDatEndpoint;
        this.timeout = timeout;
        this.retryAttempts = retryAttempts;
        this.retryDelay = retryDelay;
    }


    public Mono<UserMetaData> getExternalNotificationUserMetaData(Long userId) {
        logger.info("Invoking external user metadata API for user {}", userId);
        return webClient
                .get()
                .uri(userMetaDatEndpoint + userId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, notFoundHandler(userId))
                .onStatus(HttpStatusCode::is5xxServerError, serverErrorHandler(userId))
                .bodyToMono(UserMetaData.class)
                .timeout(Duration.ofSeconds(timeout))
                .retryWhen(Retry.backoff(retryAttempts, Duration.ofSeconds(retryDelay))
                        .filter(TimeoutException.class::isInstance)
                )
                .doOnSuccess(metaData -> logger.info("Successfully retrieved user metadata for user {}", userId))
                .onErrorResume(e -> {
                    logger.error("Error occurred fetching user-metadata for user {} : {}", userId, e.getMessage(), e);
                    return Mono.error(new IllegalArgumentException(e.getMessage()));
                });
    }

    private Function<ClientResponse, Mono<? extends Throwable>> notFoundHandler(Long userId) {
        return response -> {
            logger.warn("{} : User with ID {} not found in external service", response.statusCode(), userId);
            return Mono.error(new IllegalArgumentException("User not found with ID: " + userId));
        };
    }

    private Function<ClientResponse, Mono<? extends Throwable>> serverErrorHandler(Long userId) {
        return response -> {
            logger.warn("{} : Server error response from external service {}", response.statusCode(), userId);
            return Mono.error(new IllegalArgumentException("External Server error: " + response.statusCode()));
        };
    }

}
