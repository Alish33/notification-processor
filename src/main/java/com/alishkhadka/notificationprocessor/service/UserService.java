package com.alishkhadka.notificationprocessor.service;

import com.alishkhadka.notificationprocessor.repository.entity.User;
import com.alishkhadka.notificationprocessor.service.dto.UserDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserService {
    Mono<List<UserDto>> fetchAllUsers();
    Mono<UserDto> fetchUserById(long id);
    Mono<User> saveUser(User user);
}
