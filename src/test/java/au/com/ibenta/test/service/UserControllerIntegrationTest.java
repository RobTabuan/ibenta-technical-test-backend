package au.com.ibenta.test.service;

import au.com.ibenta.test.persistence.UserEntity;
import lombok.val;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerIntegrationTest {

    @Autowired
    WebTestClient webClient;

    @Test
    @Order(1)
    @DisplayName("Create New User")
    public void createNewUser() {
        UserEntity newUser = new UserEntity()
                .setFirstName("Roberto")
                .setLastName("Tabuan")
                .setEmail("rob.tabuan@gmail.com")
                .setPassword("password");

        webClient.post().uri("/user")
                .bodyValue(newUser)
                .accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    @DisplayName("Get user by ID")
    @Order(2)
    public void getUserById() {
        webClient.get().uri("/user/1")
                .accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.firstName").isEqualTo("Roberto")
                .jsonPath("$.lastName").isEqualTo("Tabuan")
                .jsonPath("$.email").isEqualTo("rob.tabuan@gmail.com")
                .jsonPath("$.password").isEqualTo("password");
    }

    @Test
    @DisplayName("Update user: Set Firstname to 'Rob'")
    @Order(3)
    public void updateUser() {
        UserEntity newUserValue = new UserEntity()
                .setId(1L)
                .setFirstName("Rob")
                .setLastName("Tabuan")
                .setEmail("rob.tabuan@gmail.com")
                .setPassword("newPassword");

        webClient.put().uri("/user")
                .accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json")
                .bodyValue(newUserValue)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.firstName").isEqualTo("Rob")
                .jsonPath("$.lastName").isEqualTo("Tabuan")
                .jsonPath("$.email").isEqualTo("rob.tabuan@gmail.com")
                .jsonPath("$.password").isEqualTo("newPassword");
    }

    @Test
    @Order(4)
    @DisplayName("Update a Non-Existent user: must return '404 Not Found'")
    public void updateNonExistentUser() {
        UserEntity newUserValue = new UserEntity()
                .setId(200L)
                .setFirstName("Rob")
                .setLastName("Tabuan")
                .setEmail("rob.tabuan@gmail.com")
                .setPassword("newPassword");

        webClient.put().uri("/user")
                .accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json")
                .bodyValue(newUserValue)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Unable to find user with id " + newUserValue.getId());
    }

    @Test
    @Order(5)
    @DisplayName("Delete User")
    public void deleteUser() {
        UserEntity newUser = new UserEntity()
                .setFirstName("Sample")
                .setLastName("User")
                .setEmail("sample.user@gmail.com")
                .setPassword("sample_password");

        webClient.post().uri("/user")
                .bodyValue(newUser)
                .accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isCreated();

        Boolean result = webClient.delete().uri("/user/2")
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Boolean.class)
                .returnResult().getResponseBody();

        System.out.println("result = " + result);
        assertTrue(result);
    }

    @Test
    @Order(6)
    @DisplayName("Delete a Non-Existent user: : must return '404 Not Found'")
    public void deleteNonExistentUser() {
        int userId = 300;
        webClient.delete().uri("/user/" + userId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Unable to find user with id " + userId);
    }

    @Test
    @Order(7)
    @DisplayName("Get list of all users")
    public void getListOfUsers() {
        val content = webClient.get().uri("/user/list")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserEntity.class)
                .hasSize(1)
                .returnResult()
                .getResponseBodyContent();
        System.out.println("content = " + content);
    }
}
