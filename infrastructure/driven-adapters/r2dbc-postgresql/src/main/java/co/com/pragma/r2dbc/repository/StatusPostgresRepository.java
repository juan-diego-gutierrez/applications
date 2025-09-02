package co.com.pragma.r2dbc.repository;

import co.com.pragma.r2dbc.entity.StatusEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface StatusPostgresRepository extends
    ReactiveCrudRepository<StatusEntity, String>,
    ReactiveQueryByExampleExecutor<StatusEntity> {

  Mono<StatusEntity> findByName(String name);
}
