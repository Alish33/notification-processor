package com.alishkhadka.notificationprocessor.service.usecase;

import com.alishkhadka.notificationprocessor.repository.UserRepository;
import com.alishkhadka.notificationprocessor.repository.entity.User;
import com.alishkhadka.notificationprocessor.service.adapter.UserAdapter;
import com.alishkhadka.notificationprocessor.service.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    UserRepository userRepositoryMock;

    @InjectMocks
    UserUseCase userUseCaseMock;

    private User testUser;
    private UserDto testUserDto;


    @BeforeEach
    public void setup() {
        testUser = new User();
        testUser.setId(11L);
        testUser.setEmail("test@gmail.com");
        testUser.setFullName("test_fullname");
        testUser.setAddress("test address");
        testUser.setCountry("country");
        testUser.setNotificationEventContext("{}");
        testUser.setDateTime(LocalDateTime.now());

        testUserDto = UserAdapter.getDtoFromUser(testUser);
    }

    @Test
    void testFetch_allUsers() {
        when(userRepositoryMock.findAll()).thenReturn(Flux.just(testUser));

        StepVerifier.create(userUseCaseMock.fetchAllUsers())
                .expectNextMatches(users ->
                        users.size() == 1 &&
                                users.get(0).id().equals(11L) &&
                                users.get(0).email().equals("test@gmail.com")
                )
                .verifyComplete();

        verify(userRepositoryMock, times(1)).findAll();
    }

    @Test
    void testFetch_userById_onExistingUser() {
        when(userRepositoryMock.findById(testUser.getId())).thenReturn(Mono.just(testUser));

        StepVerifier.create(userUseCaseMock.fetchUserById(11L))
                .expectNextMatches(actualUserDto ->
                        actualUserDto.id().equals(testUser.getId()) &&
                                actualUserDto.email().equals(testUser.getEmail())
                )
                .verifyComplete();

        verify(userRepositoryMock, times(1)).findById(11L);
    }

    @Test
    void test_existingUser() {
        when(userRepositoryMock.existsById(testUser.getId())).thenReturn(Mono.just(true));

        StepVerifier.create(userUseCaseMock.saveUser(testUser))
                .expectErrorMatches(err -> err.getMessage().equals("User Id " + testUser.getId() + " already exists"))
                .verify();

        verify(userRepositoryMock, times(1)).existsById(testUser.getId());
    }

    @Test
    void test_saveUser() {
        when(userRepositoryMock.existsById(testUser.getId())).thenReturn(Mono.just(false));
        when(userRepositoryMock.insertUserWithId(
                testUser.getId(),
                testUser.getEmail(),
                testUser.getFullName(),
                testUser.getAddress(),
                testUser.getCountry(),
                testUser.getNotificationEventContext(),
                testUser.getDateTime()
        )).thenReturn(Mono.just(testUser));

        StepVerifier.create(userUseCaseMock.saveUser(testUser))
                .expectNext(testUser)
                .verifyComplete();
        verify(userRepositoryMock, times(1)).insertUserWithId(
                testUser.getId(),
                testUser.getEmail(),
                testUser.getFullName(),
                testUser.getAddress(),
                testUser.getCountry(),
                testUser.getNotificationEventContext(),
                testUser.getDateTime()
        );
    }
}