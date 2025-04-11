package com.alishkhadka.notificationprocessor.constant;

public class MessagingConstant {
    private MessagingConstant(){}

    public static final String KAFKA_CONFIG_NOTIFICATION_CONSUMER_GROUP = "notification-consumer";
    public static final String KAFKA_NOTIFICATION_EVENT_TOPIC = "notification-events";
    public static final String AUTO_OFFSET_RESET_CONFIG = "latest";
}
