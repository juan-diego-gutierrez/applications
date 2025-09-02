package co.com.pragma.api;

import co.com.pragma.api.dto.ApplicationDTO;
import co.com.pragma.api.dto.SuccessResponse;
import co.com.pragma.api.exception.InvalidRequestException;
import co.com.pragma.api.exception.RequestValidator;
import co.com.pragma.api.mapper.ApplicationMapper;
import co.com.pragma.security.jwt.JwtProvider;
import co.com.pragma.usecase.application.ApplicationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ApplicationHandler {

  private static final Logger logger = LoggerFactory.getLogger(ApplicationHandler.class);
  private final ApplicationUseCase applicationUseCase;
  private final ApplicationMapper applicationMapper;
  private final RequestValidator requestValidator;
  private final JwtProvider jwtProvider;

  @Operation(summary = "Save a new application", description = "This endpoint allows you to save a new application.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Application saved successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid application data")
  })
  @RequestBody(
      description = "Application details for creation",
      required = true,
      content = @Content(schema = @Schema(implementation = ApplicationDTO.class))
  )
  @PreAuthorize("hasAuthority('CLIENT')")
  public Mono<ServerResponse> saveApplication(ServerRequest serverRequest) {
    logger.info("Received request to save application");

    String token = extractToken(serverRequest);

    return serverRequest.bodyToMono(ApplicationDTO.class)
        .flatMap(requestValidator::validate)
        .flatMap(createApplicationDTO -> isOwnApplication(token, createApplicationDTO.email())
            .flatMap(isOwn -> {
              if (Boolean.FALSE.equals(isOwn)) {
                Map<String, String> errors = new HashMap<>();
                errors.put("email", "You are trying to create an application for another user");
                return Mono.error(new InvalidRequestException(errors));
              }

              return applicationUseCase.saveApplication(
                      applicationMapper.toApplication(createApplicationDTO), token)
                  .map(applicationMapper::toApplicationDTO)
                  .flatMap(applicationDTO -> ServerResponse.status(HttpStatus.CREATED)
                      .bodyValue(new SuccessResponse<>(
                          "success",
                          Collections.singletonList(applicationDTO),
                          "Application created successfully",
                          LocalDateTime.now(),
                          serverRequest.path()
                      ))
                  );
            }));
  }

  private String extractToken(ServerRequest serverRequest) {
    return serverRequest.headers().header("Authorization").stream()
        .findFirst()
        .map(header -> header.replace("Bearer ", ""))
        .orElse(null);
  }

  private Mono<Boolean> isOwnApplication(String token, String requestedEmail) {
    String subject = jwtProvider.getSubject(token);
    if (subject.equals(requestedEmail)) {
      return Mono.just(true);
    } else {
      Mono.just(false);
    }
    return Mono.just(false);
  }

  @Operation(summary = "Get all applications", description = "This endpoint allows you to get all applications.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Returns all existing applications"),
      @ApiResponse(responseCode = "404", description = "Applications not found")
  })
  public Mono<ServerResponse> getAllApplications(ServerRequest serverRequest) {
    logger.info("Received request to get all application");

    return applicationUseCase.getAllApplications()
        .collectList()
        .flatMap(applications -> {
          if (applications.isEmpty()) {
            logger.info("Applications not founded");
            return ServerResponse.notFound().build();
          } else {
            return ServerResponse.ok().bodyValue(
                new SuccessResponse<>(
                    "success",
                    applications,
                    "Applications retrieved successfully",
                    LocalDateTime.now(),
                    serverRequest.path()
                )
            );
          }
        });
  }
}
