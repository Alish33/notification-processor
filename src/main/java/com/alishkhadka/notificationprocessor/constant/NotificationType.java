package com.alishkhadka.notificationprocessor.constant;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum NotificationType {
    PUSH_NOTIFICATION("PUSH_NOTIFICATION"),
    NONE("NONE");

    final String value;
    NotificationType(String value) {
        this.value = value;
    }

    public static NotificationType getNotificationType(String messageType) {
        return Arrays.stream(NotificationType.values())
                .filter(notificationType -> notificationType.value.equals(messageType))
                .findFirst()
                .orElse(NotificationType.NONE);
    }

}

