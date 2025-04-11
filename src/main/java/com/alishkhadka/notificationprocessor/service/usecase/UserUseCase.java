package com.alishkhadka.notificationprocessor.service.usecase;

import com.alishkhadka.notificationprocessor.repository.UserRepository;
import com.alishkhadka.notificationprocessor.repository.entity.User;
import com.alishkhadka.notificationprocessor.service.UserService;
import com.alishkhadka.notificationprocessor.service.adapter.UserAdapter;
import com.alishkhadka.notificationprocessor.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserUseCase implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<List<UserDto>> fetchAllUsers() {
        return userRepository.findAll()
                .map(UserAdapter::getDtoFromUser)
                .collectList()
                .switchIfEmpty(Mono.error(new IllegalArgumentException("No users found")));
    }

    @Override
    public Mono<UserDto> fetchUserById(long id) {
        return userRepository.findById(id)
                .map(UserAdapter::getDtoFromUser)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("User Id " + id + " not found")));
    }

    @Override
    public Mono<User> saveUser(User user) {

        return userRepository.existsById(user.getId())
                .flatMap(isPresent -> {
                    if (Boolean.TRUE.equals(isPresent)) {
                        return Mono.error(new IllegalStateException("User Id " + user.getId() + " already exists"));
                    }
                    return userRepository.insertUserWithId(
                            user.getId(),
                            user.getEmail(),
                            user.getFullName(),
                            user.getAddress(),
                            user.getCountry(),
                            user.getNotificationEventContext(),
                            user.getDateTime()
                    );
                });
    }
}
