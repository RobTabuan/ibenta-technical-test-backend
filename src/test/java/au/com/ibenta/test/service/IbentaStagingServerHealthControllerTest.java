package au.com.ibenta.test.service;

import au.com.ibenta.template.BaseTestClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
public class IbentaStagingServerHealthControllerTest extends BaseTestClass {

    @Autowired
    WebTestClient webClient;

    @Test
    @DisplayName("Check Ibenta staging server health")
    public void checkStagingServerHealth() {
        webClient.get().uri("/ibenta/health")
                .accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("UP");
    }
}
