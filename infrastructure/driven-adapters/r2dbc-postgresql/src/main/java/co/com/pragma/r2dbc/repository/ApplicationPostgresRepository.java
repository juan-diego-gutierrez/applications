package co.com.pragma.r2dbc.repository;

import co.com.pragma.r2dbc.entity.ApplicationDataEntity;
import co.com.pragma.r2dbc.entity.ApplicationEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ApplicationPostgresRepository extends
    ReactiveCrudRepository<ApplicationEntity, String>,
    ReactiveQueryByExampleExecutor<ApplicationEntity> {

  Flux<ApplicationEntity> findApplicationsByEmail(String email);

  @Query("SELECT " +
      "a.id, " +
      "a.amount, " +
      "a.term, " +
      "a.email, " +
      "at.name AS application_type, " +
      "at.interest_rate, " +
      "s.name AS status, " +
      "s.id_status " +
      "FROM applications a " +
      "LEFT JOIN application_types at ON a.id_application_type = at.id_application_type " +
      "LEFT JOIN statuses s ON a.id_status = s.id_status " +
      "WHERE (s.id_status = $1 OR $1 IS NULL) AND (at.id_application_type = $2 OR $2 IS NULL) " +
      "LIMIT $3 OFFSET $4")
  Flux<ApplicationDataEntity> findApplicationsByStatusAndApplicationType(Long statusId,
      Long applicationTypeId, int size, int offset);
}
