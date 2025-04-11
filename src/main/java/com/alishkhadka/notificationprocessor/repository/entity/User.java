package com.alishkhadka.notificationprocessor.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    private Long id;
    @Column("email")
    private String email;
    @Column("full_name")
    private String fullName;
    @Column("address")
    private String address;
    @Column("country")
    private String country;
    @Column("notification_event_context")
    private String notificationEventContext;
    @Column("doe")
    private LocalDateTime dateTime;
}
