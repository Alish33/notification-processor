package com.alishkhadka.notificationprocessor.service.adapter;

import com.alishkhadka.notificationprocessor.messaging.dto.NotificationMessageDto;
import com.alishkhadka.notificationprocessor.repository.entity.User;
import com.alishkhadka.notificationprocessor.service.dto.UserDto;
import com.alishkhadka.notificationprocessor.service.metadata.UserMetaData;
import com.alishkhadka.notificationprocessor.utils.JsonUtils;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public class UserAdapter {

    private UserAdapter() {
    }

    public static Mono<User> getUserFromMetaData(UserMetaData userMetaData, NotificationMessageDto messageDto) {
        return Mono.just(new User(
                userMetaData.userId,
                userMetaData.email,
                userMetaData.firstName + "_" + userMetaData.lastName,
                userMetaData.address,
                userMetaData.country,
                JsonUtils.toJson(messageDto),
                LocalDateTime.now()
        ));
    }

    public static UserDto getDtoFromUser(User user) {
        var builder = UserDto.builder();
        if (user == null) return builder.build();

        var notificationEvent = user.getNotificationEventContext() == null
                ? new NotificationMessageDto()
                : JsonUtils.fromJson(user.getNotificationEventContext(), NotificationMessageDto.class);

        return builder.id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .address(user.getAddress())
                .country(user.getCountry())
                .notificationEventContext(notificationEvent)
                .dateTime(user.getDateTime())
                .build();
    }
}
