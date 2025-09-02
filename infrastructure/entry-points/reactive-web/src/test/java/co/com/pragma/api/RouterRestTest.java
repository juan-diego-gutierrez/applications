package co.com.pragma.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import co.com.pragma.api.dto.ApplicationDTO;
import co.com.pragma.api.dto.SuccessResponse;
import co.com.pragma.api.exception.RequestValidator;
import co.com.pragma.api.mapper.ApplicationMapper;
import co.com.pragma.model.application.Application;
import co.com.pragma.security.jwt.JwtProvider;
import co.com.pragma.usecase.application.ApplicationUseCase;
import java.math.BigDecimal;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ContextConfiguration(classes = {RouterRest.class, ApplicationHandler.class})
@WebFluxTest(excludeAutoConfiguration = {ReactiveSecurityAutoConfiguration.class})
class RouterRestTest {

  @Autowired
  private WebTestClient webTestClient;
  @MockitoBean
  private ApplicationUseCase applicationUseCase;
  @MockitoBean
  private ApplicationMapper applicationMapper;
  @MockitoBean
  private RequestValidator requestValidator;
  @MockitoBean
  private JwtProvider jwtProvider;

  @Test
  void testSaveApplication_Success() {
    Application application = new Application(BigDecimal.valueOf(1000000), 6,
        "john.doe@example.com", 1L, 1L);

    ApplicationDTO applicationDTO = new ApplicationDTO(BigDecimal.valueOf(1000000), 6,
        "john.doe@example.com", 1L, 1L);

    when(requestValidator.validate(any())).thenReturn(Mono.just(applicationDTO));
    when(jwtProvider.getSubject(any())).thenReturn("john.doe@example.com");
    when(applicationMapper.toApplication(applicationDTO)).thenReturn(application);
    when(applicationMapper.toApplicationDTO(application)).thenReturn(applicationDTO);
    when(applicationUseCase.saveApplication(any(Application.class), any())).thenReturn(
        Mono.just(application));

    webTestClient.post()
        .uri("/api/v1/applications")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(application)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(SuccessResponse.class)
        .value(response -> Assertions.assertThat(response.status()).isEqualTo("success"));
  }

  @Test
  void testGetAllApplications_Success() {
    Application application1 = new Application(BigDecimal.valueOf(1000000), 6,
        "john.doe@example.com", 1L, 1L);
    Application application2 = new Application(BigDecimal.valueOf(1000000), 12,
        "john.doe@example2.com", 2L, 1L);

    when(applicationUseCase.getAllApplications()).thenReturn(
        Flux.just(application1, application2));

    webTestClient.get()
        .uri("/api/v1/applications")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(Application.class);
  }
}
