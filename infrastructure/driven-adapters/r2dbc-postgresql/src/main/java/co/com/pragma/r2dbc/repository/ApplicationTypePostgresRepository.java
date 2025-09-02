package co.com.pragma.r2dbc.repository;

import co.com.pragma.r2dbc.entity.ApplicationTypeEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ApplicationTypePostgresRepository extends
    ReactiveCrudRepository<ApplicationTypeEntity, String>,
    ReactiveQueryByExampleExecutor<ApplicationTypeEntity> {

  Mono<ApplicationTypeEntity> findApplicationTypeById(Long id);
}
