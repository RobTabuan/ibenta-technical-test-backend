package au.com.ibenta.test.service;

import au.com.ibenta.test.persistence.UserEntity;

import java.util.List;

public interface UserService {
    UserEntity create(UserEntity newUser);

    UserEntity get(Long userId);

    UserEntity update(UserEntity user);

    boolean delete(Long userId);

    List<UserEntity> list();
}
