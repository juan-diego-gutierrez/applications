package co.com.pragma.r2dbc.adapter;

import co.com.pragma.consumer.UserService;
import co.com.pragma.model.application.Application;
import co.com.pragma.model.application.ApplicationData;
import co.com.pragma.model.application.gateways.ApplicationRepository;
import co.com.pragma.r2dbc.repository.ApplicationPostgresRepository;
import co.com.pragma.r2dbc.entity.ApplicationEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import co.com.pragma.usecase.application.exception.BusinessException;
import co.com.pragma.usecase.application.exception.ErrorCode;
import java.util.Map;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ApplicationPostgresAdapter extends
    ReactiveAdapterOperations<Application, ApplicationEntity, String, ApplicationPostgresRepository> implements
    ApplicationRepository {

  private final UserService userService;

  public ApplicationPostgresAdapter(ApplicationPostgresRepository repository, ObjectMapper mapper,
      UserService userService) {
    super(repository, mapper, d -> mapper.map(d, Application.class));
    this.userService = userService;
  }

  @Override
  public Mono<Application> saveApplication(Application application) {
    return repository.save(toData(application)).map(this::toEntity);
  }

  @Override
  public Flux<ApplicationData> getAllApplications(Long statusId, Long applicationTypeId, int page,
      int size, String token) {
    int offset = page * size;

    return repository.findApplicationsByStatusAndApplicationType(statusId, applicationTypeId, size,
            offset)
        .flatMap(applicationDataEntity -> {
          ApplicationData applicationData = mapper.map(applicationDataEntity,
              ApplicationData.class);
          return addUserData(applicationData, token);
        })
        .switchIfEmpty(Flux.error(new BusinessException(
            Map.of("applications", ErrorCode.APPLICATIONS_NOT_FOUND.getMessage()))));
  }

  private Mono<ApplicationData> addUserData(ApplicationData applicationData, String token) {
    return userService.getUserByEmail(applicationData.getEmail(), token)
        .map(userEntity -> {
          applicationData.setName(userEntity.getName());
          applicationData.setBaseSalary(userEntity.getBaseSalary());
          return applicationData;
        });
  }
}
