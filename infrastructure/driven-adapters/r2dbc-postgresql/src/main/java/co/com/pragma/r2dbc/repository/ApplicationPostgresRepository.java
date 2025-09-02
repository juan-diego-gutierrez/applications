package co.com.pragma.r2dbc.repository;

import co.com.pragma.r2dbc.entity.ApplicationEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ApplicationPostgresRepository extends
    ReactiveCrudRepository<ApplicationEntity, String>,
    ReactiveQueryByExampleExecutor<ApplicationEntity> {

  Flux<ApplicationEntity> findApplicationsByEmail(String email);
}
