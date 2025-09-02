package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.adapter.ApplicationTypePostgresAdapter;
import co.com.pragma.r2dbc.repository.ApplicationTypePostgresRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class ApplicationTypePostgresAdapterTest {

  @InjectMocks
  ApplicationTypePostgresAdapter repositoryAdapter;

  @Mock
  ApplicationTypePostgresRepository repository;

  @Mock
  ObjectMapper mapper;
}
