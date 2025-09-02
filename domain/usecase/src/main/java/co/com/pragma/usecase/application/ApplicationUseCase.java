package co.com.pragma.usecase.application;

import co.com.pragma.model.application.Application;
import co.com.pragma.model.application.gateways.ApplicationRepository;
import co.com.pragma.model.applicationtype.gateways.ApplicationTypeRepository;
import co.com.pragma.model.status.gateways.StatusRepository;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.application.exception.BusinessException;
import co.com.pragma.usecase.application.exception.ErrorCode;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ApplicationUseCase {

  private static final String PENDING_REVIEW = "pending_review";
  private final ApplicationRepository applicationRepository;
  private final ApplicationTypeRepository applicationTypeRepository;
  private final StatusRepository statusRepository;
  private final UserRepository userRepository;

  public Mono<Application> saveApplication(Application application, String token) {
    return applicationTypeRepository.applicationTypeExists(application.getApplicationTypeId())
        .flatMap(exists -> {
          if (Boolean.FALSE.equals(exists)) {
            return Mono.error(new BusinessException(
                Map.of("applicationType", ErrorCode.TYPE_NOT_FOUND.getMessage())));
          }

          return userRepository.getUserByEmail(application.getEmail(), token)
              .flatMap(userResponse -> statusRepository.getStatusByName(PENDING_REVIEW)
                  .flatMap(initialStatus -> {
                    application.setStatusId(initialStatus.getId());
                    return applicationRepository.saveApplication(application);
                  }))
              .onErrorResume(e -> Mono.error(
                  new BusinessException(Map.of("user", ErrorCode.USER_NOT_FOUND.getMessage()))));
        });
  }

  public Flux<Application> getAllApplications() {
    return applicationRepository.getAllApplications();
  }

}
