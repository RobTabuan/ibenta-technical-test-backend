package au.com.ibenta.test.service;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Api(tags = "Ibenta")
@RestController
@RequestMapping("/ibenta")
public class IbentaStagingServerHealthController {
    private final WebClient webClient;

    @Autowired
    public IbentaStagingServerHealthController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> ibentaServerHealthCheck() {
        return Mono.create(sink -> {
            this.webClient.method(HttpMethod.GET)
                    .uri("http://authentication-service.staging.ibenta.com/actuator/health")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .subscribe(clientResponse -> clientResponse.toEntity(String.class)
                            .doFinally((s) -> sink.success())
                            .subscribe(sink::success)
                    );

        });
    }
}
