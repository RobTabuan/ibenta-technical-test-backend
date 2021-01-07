package au.com.ibenta.test.service;

import au.com.ibenta.test.persistence.UserEntity;
import au.com.ibenta.test.persistence.UserRepository;
import lombok.val;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceUnitTest {

    UserRepository userRepo;

    UserService userService;

    UserEntity userStub;

    @BeforeEach
    public void init() {
        this.userRepo = mock(UserRepository.class);
        this.userService = spy(new UserServiceImpl(this.userRepo));
        this.userStub = new UserEntity()
                .setId(1L)
                .setFirstName("Roberto")
                .setLastName("Tabuan")
                .setEmail("rob.tabuan@gmail.com")
                .setPassword("password");
    }

    @Test
    @DisplayName(" Create New User")
    @Order(1)
    public void createNewUser() {

        when(this.userRepo.save(any(UserEntity.class)))
                .thenReturn(this.userStub);

        UserEntity createdUser = this.userService.create(this.userStub);

        //make sure that "save" method was called once
        verify(this.userRepo, times(1)).save(any(UserEntity.class));

        assertEquals(createdUser, this.userStub);
        System.out.println("Created User: " + createdUser);
    }

    @Test
    @DisplayName("Get user by ID")
    @Order(2)
    public void getUserById() {
        val userId = 1L;

        when(this.userRepo.findById(userId)).thenReturn(
                ofNullable(this.userStub)
        );

        val foundUser = this.userService.get(userId);

        verify(this.userRepo, times(1)).findById(userId);
        assertTrue(foundUser.isPresent());
        System.out.println("Found User: " + foundUser);
    }


    @Test
    @DisplayName("Update user: Set Firstname to 'Rob'")
    @Order(3)
    public void updateUser() {
        val updatedUserStub = new UserEntity()
                .setId(this.userStub.getId())
                .setFirstName("Rob")
                .setLastName(this.userStub.getLastName())
                .setEmail(this.userStub.getEmail())
                .setPassword(this.userStub.getPassword());

        when(this.userRepo.save(any(UserEntity.class))).thenReturn(
                updatedUserStub
        );

        when(this.userRepo.findById(this.userStub.getId()))
                .thenReturn(
                        ofNullable(this.userStub)
                );

        val updatedUser = this.userService.update(this.userStub.getId(), this.userStub);

        verify(this.userRepo, times(1)).save(any(UserEntity.class));
        verify(this.userService, times(1)).get(this.userStub.getId());
        assertEquals(updatedUserStub.getFirstName(), updatedUser.getFirstName());

        System.out.println("Old value: " + this.userStub);
        System.out.println("New value: " + updatedUser);

    }

    @Test
    @Order(4)
    @DisplayName("Update a Non-Existent user: must throw 'UserNotFound'")
    public void updateNonExistentUser() {
        //simulate empty result from database
        when(this.userRepo.findById(any()))
                .thenReturn(Optional.empty());
        try {
            this.userService.update(2L, userStub);
        } catch (UserNotFound userNotFound) {
            verify(this.userService, times(1)).get(any(Long.class));
            assertEquals("Unable to find user with id " + 2L, userNotFound.getMessage());
            System.out.println("Exception Message");
            System.out.println(userNotFound.getMessage());
        }
    }

    @Test
    @Order(5)
    @DisplayName("Delete User")
    public void deleteUser() {
        val uid = userStub.getId();
        when(this.userRepo.findById(any()))
                .thenReturn(
                        ofNullable(this.userStub)
                );

        val result = this.userService.delete(uid);

        verify(this.userService, times(1)).get(any(Long.class));
        assertTrue(result);
    }

    @Test
    @Order(6)
    @DisplayName("Delete a Non-Existent user: must throw 'UserNotFound'")
    public void deleteNonExistentUser() {
        //simulate empty result from database
        when(this.userRepo.findById(any())).thenReturn(Optional.empty());

        try {
            this.userService.delete(2L);
        } catch (UserNotFound userNotFound) {
            verify(this.userService, times(1)).get(any(Long.class));
            verify(this.userService, times(1)).delete(any(Long.class));
            assertEquals("Unable to find user with id " + 2L, userNotFound.getMessage());
            System.out.println("Exception Message");
            System.out.println(userNotFound.getMessage());
        }
    }

    @Test
    @Order(7)
    @DisplayName("Get list of users")
    public void getListOfUsers() {
        val userListStub = new ArrayList<UserEntity>() {{
            add(userStub);
            add(new UserEntity()
                    .setId(1L)
                    .setFirstName("Sample")
                    .setLastName("User")
                    .setEmail("sample.user@gmail.com")
                    .setPassword("user_password"));
        }};
        when(this.userRepo.findAll()).thenReturn(userListStub);

        val result = this.userService.list();

        verify(this.userRepo, times(1)).findAll();
        assertEquals(2, result.size());
        System.out.println("Found Users: " + result);
    }
}
