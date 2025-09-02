package co.com.pragma.model.applicationtype.gateways;

import co.com.pragma.model.applicationtype.ApplicationType;
import reactor.core.publisher.Mono;

public interface ApplicationTypeRepository {

  Mono<ApplicationType> getApplicationTypeById(Long id);

  Mono<Boolean> applicationTypeExists(Long id);
}
