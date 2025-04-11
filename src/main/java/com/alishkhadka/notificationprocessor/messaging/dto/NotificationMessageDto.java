package com.alishkhadka.notificationprocessor.messaging.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationMessageDto {
    @JsonProperty("userId")
    private Long userId;
    @JsonProperty("messageType")
    private String messageType;
    @JsonProperty("content")
    private String content;

    @Override
    public String toString() {
        return "NotificationMessageDto{" +
                "userId=" + userId +
                ", messageType='" + messageType + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}





