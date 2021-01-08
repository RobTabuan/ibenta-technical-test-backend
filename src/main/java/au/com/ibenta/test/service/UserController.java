package au.com.ibenta.test.service;

import au.com.ibenta.test.persistence.UserEntity;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Api(tags = "User")
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserEntity> create(@RequestBody UserEntity userEntity) {
        return this.service.create(userEntity);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserEntity> get(@PathVariable Long userId) {
        return this.service.get(userId);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserEntity> update(@RequestBody UserEntity user) {
        return this.service.update(user);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Boolean> delete(@PathVariable Long userId) {
        return this.service.delete(userId);
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<UserEntity> list() {
        return this.service.list();
    }

}
