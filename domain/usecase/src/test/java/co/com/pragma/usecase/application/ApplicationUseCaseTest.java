package co.com.pragma.usecase.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import co.com.pragma.model.application.Application;
import co.com.pragma.model.application.ApplicationData;
import co.com.pragma.model.application.gateways.ApplicationRepository;
import co.com.pragma.model.applicationtype.ApplicationType;
import co.com.pragma.model.applicationtype.gateways.ApplicationTypeRepository;
import co.com.pragma.model.status.Status;
import co.com.pragma.model.status.gateways.StatusRepository;
import co.com.pragma.model.user.UserResponse;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.application.exception.BusinessException;
import co.com.pragma.usecase.application.exception.ErrorCode;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ApplicationUseCaseTest {

  private ApplicationUseCase applicationUseCase;
  private ApplicationRepository applicationRepository;
  private ApplicationTypeRepository applicationTypeRepository;
  private StatusRepository statusRepository;
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    applicationRepository = Mockito.mock(ApplicationRepository.class);
    applicationTypeRepository = Mockito.mock(ApplicationTypeRepository.class);
    statusRepository = Mockito.mock(StatusRepository.class);
    userRepository = Mockito.mock(UserRepository.class);
    applicationUseCase = new ApplicationUseCase(applicationRepository, applicationTypeRepository,
        statusRepository, userRepository);
  }

  @Test
  void testSaveApplication_Success() {
    Application application = new Application(BigDecimal.valueOf(1000000), 6,
        "john.doe@example.com", 1L, 1L);

    when(applicationTypeRepository.applicationTypeExists(
        application.getApplicationTypeId())).thenReturn(Mono.just(true));

    when(userRepository.getUserByEmail(application.getEmail(), "token")).thenReturn(
        Mono.just(new UserResponse("John", "Doe", any(), "", "", application.getEmail(),
            BigDecimal.valueOf(1000000))));

    when(statusRepository.getStatusByName("pending_review")).thenReturn(
        Mono.just(new Status(1L, "pending_review", "Pending Review")));

    when(applicationRepository.saveApplication(any(Application.class))).thenReturn(
        Mono.just(application));

    StepVerifier.create(applicationUseCase.saveApplication(application, "token"))
        .expectNext(application)
        .verifyComplete();
  }

  @Test
  void testSaveApplication_ApplicationTypeNotFound() {
    Application application = new Application();
    application.setApplicationTypeId(3L);

    when(applicationTypeRepository.applicationTypeExists(
        application.getApplicationTypeId())).thenReturn(Mono.just(false));

    StepVerifier.create(applicationUseCase.saveApplication(application, "token"))
        .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
            ((BusinessException) throwable).getErrors().containsKey("applicationType") &&
            ((BusinessException) throwable).getErrors().get("applicationType")
                .equals(ErrorCode.TYPE_NOT_FOUND.getMessage()))
        .verify();
  }

  @Test
  void testSaveApplication_UserNotFound() {
    Application application = new Application();
    application.setEmail("nonexistent@example.com");
    application.setApplicationTypeId(1L);

    when(applicationTypeRepository.applicationTypeExists(
        application.getApplicationTypeId())).thenReturn(Mono.just(true));

    when(userRepository.getUserByEmail(application.getEmail(), "token")).thenReturn(
        Mono.error(new Throwable()));

    StepVerifier.create(applicationUseCase.saveApplication(application, "token"))
        .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
            ((BusinessException) throwable).getErrors().containsKey("user") &&
            ((BusinessException) throwable).getErrors().get("user")
                .equals(ErrorCode.USER_NOT_FOUND.getMessage()))
        .verify();
  }

  @Test
  void testGetAllApplications_WithStatusAndType() {
    ApplicationData application1 = new ApplicationData(BigDecimal.valueOf(1000000), 6,
        "john.doe@example.com", "John", "Vehicle", BigDecimal.valueOf(10), "pending_review",
        BigDecimal.valueOf(1000000), BigDecimal.valueOf(200000));

    ApplicationData application2 = new ApplicationData(BigDecimal.valueOf(1000000), 12,
        "john.doe@example.com", "John", "Vehicle", BigDecimal.valueOf(10), "pending_review",
        BigDecimal.valueOf(1000000), BigDecimal.valueOf(300000));

    Mono<Status> statusMono = Mono.just(new Status(1L, "pending_review", "Pending Review"));
    Mono<ApplicationType> applicationTypeMono = Mono.just(
        new ApplicationType(1L, "vehicle", BigDecimal.valueOf(0), BigDecimal.valueOf(10000000),
            BigDecimal.valueOf(10), true));
    Flux<ApplicationData> applicationDataFlux = Flux.just(application1, application2);
    Flux<ApplicationData> emptyFlux = Flux.empty();

    statusMono.switchIfEmpty(Mono.empty());
    applicationTypeMono.switchIfEmpty(Mono.empty());
    applicationDataFlux.switchIfEmpty(Mono.empty());
    emptyFlux.switchIfEmpty(Flux.empty());

    when(statusRepository.getStatusByName(any())).thenReturn(statusMono);
    when(applicationTypeRepository.getApplicationTypeById(any())).thenReturn(applicationTypeMono);

    when(applicationRepository.getAllApplications(1L, 1L, 0, 10, "token")).thenReturn(
        applicationDataFlux);
    when(applicationRepository.getAllApplications(null, null, 0, 10, "token")).thenReturn(
        emptyFlux);

    Flux<ApplicationData> result = applicationUseCase.getAllApplications("pending_review", 1L, 0,
        10, "token");

    StepVerifier.create(result)
        .expectNext(application1, application2)
        .verifyComplete();
  }
}
