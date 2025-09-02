package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.adapter.StatusPostgresAdapter;
import co.com.pragma.r2dbc.repository.StatusPostgresRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class StatusPostgresAdapterTest {

  @InjectMocks
  StatusPostgresAdapter repositoryAdapter;

  @Mock
  StatusPostgresRepository repository;

  @Mock
  ObjectMapper mapper;
}
