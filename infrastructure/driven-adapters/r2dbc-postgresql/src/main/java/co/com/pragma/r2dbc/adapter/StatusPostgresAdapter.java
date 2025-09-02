package co.com.pragma.r2dbc.adapter;

import co.com.pragma.model.status.Status;
import co.com.pragma.model.status.gateways.StatusRepository;
import co.com.pragma.r2dbc.entity.StatusEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import co.com.pragma.r2dbc.repository.StatusPostgresRepository;
import co.com.pragma.usecase.application.exception.BusinessException;
import co.com.pragma.usecase.application.exception.ErrorCode;
import java.util.Map;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class StatusPostgresAdapter extends
    ReactiveAdapterOperations<Status, StatusEntity, String, StatusPostgresRepository> implements
    StatusRepository {

  public StatusPostgresAdapter(StatusPostgresRepository repository,
      ObjectMapper mapper) {
    super(repository, mapper, d -> mapper.map(d, Status.class));
  }

  @Override
  public Mono<Status> getStatusByName(String name) {
    return repository.findByName(name).map(this::toEntity)
        .switchIfEmpty(
            Mono.error(
                new BusinessException(Map.of("status", ErrorCode.STATUS_NOT_FOUND.getMessage()))));
  }
}
