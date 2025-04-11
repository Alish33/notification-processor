package com.alishkhadka.notificationprocessor.repository;

import com.alishkhadka.notificationprocessor.repository.entity.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {

    @Query("""
                INSERT INTO user (id, email, full_name, address, country, notification_event_context, doe)
                VALUES (:id, :em, :fn, :addr, :cntry, :ctx, :dt)
            """)
    Mono<User> insertUserWithId(
            @Param("id") Long id,
            @Param("em") String email,
            @Param("fn") String fullName,
            @Param("addr") String address,
            @Param("cntry") String country,
            @Param("ctx") String notificationEventContext,
            @Param("dt") LocalDateTime dateTime
    );
}
