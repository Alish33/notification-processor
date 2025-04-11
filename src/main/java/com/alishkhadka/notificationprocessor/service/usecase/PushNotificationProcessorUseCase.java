package com.alishkhadka.notificationprocessor.service.usecase;

import com.alishkhadka.notificationprocessor.constant.NotificationType;
import com.alishkhadka.notificationprocessor.messaging.dto.NotificationMessageDto;
import com.alishkhadka.notificationprocessor.service.NotificationProcessorService;
import com.alishkhadka.notificationprocessor.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.alishkhadka.notificationprocessor.service.adapter.UserAdapter.getUserFromMetaData;
import static io.micrometer.common.util.StringUtils.isNotBlank;

@Service
public class PushNotificationProcessorUseCase implements NotificationProcessorService {

    private final Logger logger = LoggerFactory.getLogger(PushNotificationProcessorUseCase.class);
    private final ExternalUserMetaDataUseCase userMetaDataService;
    private final UserService userService;

    public PushNotificationProcessorUseCase(ExternalUserMetaDataUseCase externalUserMetaDataUseCase,
                                            UserService userService) {
        this.userMetaDataService = externalUserMetaDataUseCase;
        this.userService = userService;
    }

    @Override
    public Mono<Void> process(NotificationMessageDto message) {

        if (!isUserIdValid(message.getUserId())) {
            logger.warn("User id is not valid");
            return Mono.error(new IllegalArgumentException("User id is not valid or is null"));
        }
        return userMetaDataService.getExternalNotificationUserMetaData(message.getUserId())
                .flatMap(userMetaData -> getUserFromMetaData(userMetaData, message))
                .flatMap(this.userService::saveUser)
                .doOnSuccess(value -> logger.info("User {} Data Saved", message.getUserId()))
                .then()
                .onErrorResume(e -> {
                    logger.warn("Error while processing external user metadata", e);
                    return Mono.error(e);
                });
    }


    private boolean isUserIdValid(Long userId) {
        return userId != null && isNotBlank(userId.toString());
    }

    @Override
    public String messageType() {
        return NotificationType.PUSH_NOTIFICATION.name();
    }

}
