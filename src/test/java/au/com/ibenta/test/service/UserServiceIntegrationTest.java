package au.com.ibenta.test.service;

import au.com.ibenta.test.persistence.UserEntity;
import au.com.ibenta.test.persistence.UserRepository;
import lombok.val;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceIntegrationTest {

    @Autowired
    UserRepository userRepo;

    @Autowired
    private UserService userService;

    @Test
    @Order(1)
    @DisplayName(" Create New User")
    public void createUser() {
        UserEntity newUser = new UserEntity()
                .setFirstName("Roberto")
                .setLastName("Tabuan")
                .setEmail("rob.tabuan@gmail.com")
                .setPassword("password");

        val createdUser = this.userService.create(newUser);
        System.out.println(createdUser);

        assertEquals(newUser.getFirstName(), createdUser.getFirstName());
        assertEquals(newUser.getLastName(), createdUser.getLastName());
        assertEquals(newUser.getEmail(), createdUser.getEmail());
        assertEquals(newUser.getPassword(), createdUser.getPassword());

    }

    @Test
    @DisplayName("Get user by ID")
    @Order(2)
    public void getUserById() {
        val userId = 1L;
        val foundUser = this.userService.get(userId);
        assertTrue(foundUser.isPresent());
        assertEquals(userId, foundUser.get().getId());
        System.out.println("Found User: " + foundUser);
    }

    @Test
    @DisplayName("Update user: Set Firstname to 'Rob'")
    @Order(3)
    public void updateUser() {
        val userId = 1L;
        val foundUser = this.userService.get(userId);
        System.out.println("Old value: " + foundUser);
        foundUser.ifPresent(userEntity -> this.userService.update(
                userEntity.getId(),
                userEntity.setFirstName("Rob")
        ));

        val updatedUser = this.userService.get(userId);
        System.out.println("New value: " + updatedUser);

        assertEquals("Rob", foundUser.get().getFirstName());
    }

    @Test
    @Order(4)
    @DisplayName("Update a Non-Existent user: must throw 'UserNotFound'")
    public void updateNonExistentUser() {
        try {
            this.userService.update(200L, null);
            fail();
        } catch (UserNotFound userNotFound) {
            System.out.println("Exception Message:");
            System.out.println(userNotFound.getMessage());
        }

    }

    @Test
    @Order(5)
    @DisplayName("Delete User")
    public void deleteUser() {
        val newUser = this.userService.create(new UserEntity()
                .setFirstName("Sample")
                .setLastName("User")
                .setEmail("sample.user@gmail.com")
                .setPassword("user_password"));

        System.out.println("User to Delete: " + newUser);

        val result = this.userService.delete(newUser.getId());
        assertTrue(result);

        val foundUser = this.userService.get(newUser.getId());
        assertFalse(foundUser.isPresent());

    }

    @Test
    @Order(6)
    @DisplayName("Delete a Non-Existent user: must throw 'UserNotFound'")
    public void deleteNonExistentUser() {
        try {
            this.userService.delete(200L);
            fail();
        } catch (UserNotFound userNotFound) {
            System.out.println("Exception Message:");
            System.out.println(userNotFound.getMessage());
        }
    }

    @Test
    @Order(7)
    @DisplayName("Get list of all users")
    public void getListOfUsers() {
        this.userService.create(new UserEntity()
                .setFirstName("Sample")
                .setLastName("User")
                .setEmail("sample.user@gmail.com")
                .setPassword("user_password"));
        val allUsers = this.userService.list();
        allUsers.forEach(System.out::println);
        assertEquals(2, allUsers.size());

    }


}
