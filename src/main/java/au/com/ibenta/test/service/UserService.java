package au.com.ibenta.test.service;

import au.com.ibenta.test.persistence.UserEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserEntity> create(UserEntity newUser);

    Mono<UserEntity> get(Long userId);

    Mono<UserEntity> update(UserEntity user) throws UserNotFound;

    Mono<Boolean> delete(Long userId) throws UserNotFound;

    Flux<UserEntity> list();
}
