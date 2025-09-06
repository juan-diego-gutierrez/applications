package co.com.pragma.model.application.gateways;

import co.com.pragma.model.application.Application;
import co.com.pragma.model.application.ApplicationData;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ApplicationRepository {

  Mono<Application> saveApplication(Application application);

  Flux<ApplicationData> getAllApplications(Long statusId, Long applicationTypeId, int page,
      int size, String token);
}
