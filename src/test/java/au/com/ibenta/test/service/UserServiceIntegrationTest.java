package au.com.ibenta.test.service;

import au.com.ibenta.test.persistence.UserEntity;
import lombok.val;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    @Order(1)
    @DisplayName("Create New User")
    public void createUser() {
        UserEntity newUser = new UserEntity()
                .setFirstName("Roberto")
                .setLastName("Tabuan")
                .setEmail("rob.tabuan@gmail.com")
                .setPassword("password");

        StepVerifier.create(this.userService.create(newUser))
                .assertNext(userEntity -> {
                    System.out.println(userEntity);
                    assertNotNull(userEntity);
                })
                .verifyComplete();

    }

    @Test
    @DisplayName("Get user by ID")
    @Order(2)
    public void getUserById() {
        val userId = 1L;
        StepVerifier.create(this.userService.get(userId))
                .assertNext(foundUser -> {
                    assertEquals(userId, foundUser.getId());
                    System.out.println("Found User: " + foundUser);
                })
                .verifyComplete();

    }

    @Test
    @DisplayName("Update user: Set Firstname to 'Rob'")
    @Order(3)
    public void updateUser() {
        val userId = 1L;
        StepVerifier.create(this.userService.get(userId).flatMap(foundUser -> {
            System.out.println("Old value: " + foundUser);
            return this.userService.update(foundUser.setId(userId).setFirstName("Rob"));
        })).assertNext(userEntity -> {
            assertEquals("Rob", userEntity.getFirstName());
            System.out.println("New value: " + userEntity);
        }).verifyComplete();
    }

    @Test
    @Order(4)
    @DisplayName("Update a Non-Existent user: must throw 'UserNotFound'")
    public void updateNonExistentUser() {

        StepVerifier.create(this.userService.update(new UserEntity().setId(200L)))
                .expectError(UserNotFound.class)
                .verify();
    }

    @Test
    @Order(5)
    @DisplayName("Delete User")
    public void deleteUser() {
        StepVerifier.create(this.userService.create(new UserEntity()
                        .setFirstName("Sample")
                        .setLastName("User")
                        .setEmail("sample.user@gmail.com")
                        .setPassword("user_password")
                ).flatMap(user -> this.userService.delete(user.getId()))
        ).assertNext(Assertions::assertTrue)
                .verifyComplete();
    }

    @Test
    @Order(6)
    @DisplayName("Delete a Non-Existent user: must throw 'UserNotFound'")
    public void deleteNonExistentUser() {
        StepVerifier.create(this.userService.delete(200L))
                .expectError(UserNotFound.class)
                .verify();
    }

    @Test
    @Order(7)
    @DisplayName("Get list of all users")
    public void getListOfUsers() {
        StepVerifier.create(this.userService.list())
                .expectNextCount(1)
                .verifyComplete();

    }

}
