package au.com.ibenta.test.service;

import au.com.ibenta.test.persistence.UserEntity;
import au.com.ibenta.test.persistence.UserRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    @Autowired
    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserEntity create(UserEntity newUser) {
        return this.userRepo.save(newUser.setId(null));
    }

    @Override
    public Optional<UserEntity> get(Long userId) {
        return this.userRepo.findById(userId);
    }

    @Override
    public UserEntity update(Long userId, UserEntity user) throws UserNotFound {
        val foundUser = this.get(userId).orElseThrow(() -> new UserNotFound("Unable to find user with id " + userId));
        return this.userRepo.save(
                foundUser.setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
        );
    }

    @Override
    public boolean delete(Long userId) throws UserNotFound{
        this.get(userId).orElseThrow(() -> new UserNotFound("Unable to find user with id " + userId));
        this.userRepo.deleteById(userId);
        return true;
    }

    @Override
    public List<UserEntity> list() {
        return this.userRepo.findAll();
    }
}
