package co.com.pragma.r2dbc.adapter;

import co.com.pragma.model.application.Application;
import co.com.pragma.model.application.gateways.ApplicationRepository;
import co.com.pragma.r2dbc.repository.ApplicationPostgresRepository;
import co.com.pragma.r2dbc.entity.ApplicationEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ApplicationPostgresAdapter extends
    ReactiveAdapterOperations<Application, ApplicationEntity, String, ApplicationPostgresRepository> implements
    ApplicationRepository {

  public ApplicationPostgresAdapter(ApplicationPostgresRepository repository, ObjectMapper mapper) {
    super(repository, mapper, d -> mapper.map(d, Application.class));
  }

  @Override
  public Mono<Application> saveApplication(Application application) {
    return repository.save(toData(application)).map(this::toEntity);
  }

  @Override
  public Flux<Application> getAllApplications() {
    return repository.findAll().map(this::toEntity);
  }
}
