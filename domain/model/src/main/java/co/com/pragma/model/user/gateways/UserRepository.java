package co.com.pragma.model.user.gateways;

import co.com.pragma.model.user.UserResponse;
import reactor.core.publisher.Mono;

public interface UserRepository {

  Mono<UserResponse> getUserByEmail(String email);
}
