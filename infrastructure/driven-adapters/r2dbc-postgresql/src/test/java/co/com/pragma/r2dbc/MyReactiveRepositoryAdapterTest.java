package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.adapter.ApplicationPostgresAdapter;
import co.com.pragma.r2dbc.repository.ApplicationPostgresRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ApplicationPostgresAdapterTest {

  @InjectMocks
  ApplicationPostgresAdapter repositoryAdapter;

  @Mock
  ApplicationPostgresRepository repository;

  @Mock
  ObjectMapper mapper;

}
