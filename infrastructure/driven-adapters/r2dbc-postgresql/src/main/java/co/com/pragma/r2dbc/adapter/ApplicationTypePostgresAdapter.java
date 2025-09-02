package co.com.pragma.r2dbc.adapter;

import co.com.pragma.model.applicationtype.ApplicationType;
import co.com.pragma.model.applicationtype.gateways.ApplicationTypeRepository;
import co.com.pragma.r2dbc.entity.ApplicationTypeEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import co.com.pragma.r2dbc.repository.ApplicationTypePostgresRepository;
import co.com.pragma.usecase.application.exception.BusinessException;
import co.com.pragma.usecase.application.exception.ErrorCode;
import java.util.Map;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ApplicationTypePostgresAdapter extends
    ReactiveAdapterOperations<ApplicationType, ApplicationTypeEntity, String, ApplicationTypePostgresRepository> implements
    ApplicationTypeRepository {

  public ApplicationTypePostgresAdapter(ApplicationTypePostgresRepository repository,
      ObjectMapper mapper) {
    super(repository, mapper, d -> mapper.map(d, ApplicationType.class));
  }

  @Override
  public Mono<ApplicationType> getApplicationTypeById(Long id) {
    return repository.findApplicationTypeById(id).map(this::toEntity)
        .switchIfEmpty(
            Mono.error(new BusinessException(
                Map.of("applicationType", ErrorCode.TYPE_NOT_FOUND.getMessage()))));
  }

  @Override
  public Mono<Boolean> applicationTypeExists(Long id) {
    return repository.findApplicationTypeById(id)
        .map(applicationTypeEntity -> true)
        .defaultIfEmpty(false);
  }
}
