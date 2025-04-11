package com.alishkhadka.notificationprocessor.service.dto;

import com.alishkhadka.notificationprocessor.messaging.dto.NotificationMessageDto;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserDto(
        Long id,
        String email,
        String fullName,
        String address,
        String country,
        NotificationMessageDto notificationEventContext,
        LocalDateTime dateTime
) {
}
