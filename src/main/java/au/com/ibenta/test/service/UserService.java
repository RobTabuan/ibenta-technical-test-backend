package au.com.ibenta.test.service;

import au.com.ibenta.test.persistence.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserEntity create(UserEntity newUser);

    Optional<UserEntity> get(Long userId);

    UserEntity update(Long userId, UserEntity user) throws UserNotFound;

    boolean delete(Long userId) throws UserNotFound;

    List<UserEntity> list();
}
