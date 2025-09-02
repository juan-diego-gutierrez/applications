package co.com.pragma.consumer;

import co.com.pragma.model.user.UserResponse;
import co.com.pragma.model.user.gateways.UserRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService implements UserRepository {

  private final WebClient client;

  @CircuitBreaker(name = "userService")
  public Mono<UserResponse> getUserByEmail(String email, String token) {
    return client
        .get()
        .uri("/api/v1/users/{email}", email)
        .header("Authorization", "Bearer " + token)
        .retrieve()
        .bodyToMono(UserResponse.class);
  }
}
