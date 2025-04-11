package com.alishkhadka.notificationprocessor.controller;

import com.alishkhadka.notificationprocessor.repository.entity.User;
import com.alishkhadka.notificationprocessor.service.UserService;
import com.alishkhadka.notificationprocessor.utils.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Mono<RestResponse> getAllUser() {
        return userService.fetchAllUsers()
                .map(RestResponse::success)
                .onErrorResume(error -> Mono.just(RestResponse.error(error.getMessage())));
    }

    @GetMapping("/{id}")
    public Mono<RestResponse> getUserById(@PathVariable long id) {
        return userService
                .fetchUserById(id)
                .map(RestResponse::success)
                .onErrorResume(err -> Mono.just(RestResponse.error(err.getMessage())));
    }

    @PostMapping("/save")
    public Mono<RestResponse> saveUser(@RequestBody User user) {
        return userService.saveUser(user)
                .map(RestResponse::success)
                .onErrorResume(err -> Mono.just(RestResponse.error(err.getMessage())));
    }

}
