package au.com.ibenta.test.service;

import au.com.ibenta.test.persistence.UserEntity;
import au.com.ibenta.test.persistence.UserRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.util.StringUtils.hasLength;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    @Autowired
    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Mono<UserEntity> create(UserEntity newUser) {
        return Mono.just(this.userRepo.save(newUser));
    }

    @Override
    public Mono<UserEntity> get(Long userId) {
        return Mono.create(sink -> {
            val foundUser = this.userRepo.findById(userId);
            foundUser.ifPresent(sink::success);
            sink.success();
        });
    }

    @Override
    public Mono<UserEntity> update(UserEntity user) throws UserNotFound {
        val userId = user.getId();
        return this.get(userId)
                .switchIfEmpty(Mono.error(() -> new UserNotFound("Unable to find user with id " + userId)))
                .map(foundUser -> {

                    if (hasLength(user.getFirstName()))
                        foundUser.setFirstName(user.getFirstName());
                    if (hasLength(user.getLastName()))
                        foundUser.setLastName(user.getLastName());
                    if(hasLength(user.getEmail()))
                        foundUser.setEmail(user.getEmail());
                    if (hasLength(user.getPassword()))
                        foundUser.setPassword(user.getPassword());

                    return this.userRepo.save(foundUser);
                });
    }

    @Override
    public Mono<Boolean> delete(Long userId) throws UserNotFound {
        return this.checkUserById(userId)
                .map(foundUser -> {
                    this.userRepo.delete(foundUser);
                    return true;
                });
    }

    @Override
    public Flux<UserEntity> list() {
        return Flux.fromIterable(this.userRepo.findAll());
    }

    private Mono<UserEntity> checkUserById(Long userId) throws UserNotFound {
        return this.get(userId).switchIfEmpty(
                Mono.error(() -> new UserNotFound("Unable to find user with id " + userId))
        );
    }
}
