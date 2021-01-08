package au.com.ibenta.test.service;

import au.com.ibenta.test.model.ChangePasswordRequest;
import au.com.ibenta.test.model.User;
import au.com.ibenta.test.persistence.UserEntity;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Api(tags = "User")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final PasswordEncoder pwdEncoder;
    private final UserService service;

    @Autowired
    public UserController(PasswordEncoder pwdEncoder, UserService service) {
        this.pwdEncoder = pwdEncoder;
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> create(@RequestBody @Valid User user) {
        val newUser = user.toUserEntity();
        val encodedPassword = this.pwdEncoder.encode(newUser.getPassword());
        newUser.setPassword(encodedPassword);
        return this.service.create(newUser)
                .map(createdUser -> {
                    log.debug("User Created: {}", createdUser);
                    return createdUser;
                })
                .map(User::fromUserEntity);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<User> get(@PathVariable Long userId) {
        return this.service.get(userId).map(User::fromUserEntity);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<User> update(@RequestBody @Valid User user) {
        val newUser = user.toUserEntity();
        /*
         * Always clear the password in this endpoint.
         * Use another endpoint to change password.
         */
        newUser.setPassword(null);
        return this.service.update(user.toUserEntity())
                .map(updatedUser -> {
                    log.debug("User Updated: {}", updatedUser);
                    return updatedUser;
                })
                .map(User::fromUserEntity);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Boolean> delete(@PathVariable Long userId) {
        return this.service.delete(userId)
                .map(result -> {
                    log.debug("User with id '{}' was deleted", userId);
                    return result;
                });
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<User> list() {
        return this.service.list().map(User::fromUserEntity);
    }

    @PatchMapping(value = "/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> changePassword(@RequestBody ChangePasswordRequest request) {
        val user = new UserEntity()
                .setId(request.getUserId())
                .setPassword(this.pwdEncoder.encode(request.getNewPassword()));
        return this.service.update(user)
                .flatMap(userEntity -> {
                    log.debug("Password Changed: userId {}, new password {}", userEntity.getId(), userEntity.getPassword());
                    return Mono.empty();
                });
    }
}
