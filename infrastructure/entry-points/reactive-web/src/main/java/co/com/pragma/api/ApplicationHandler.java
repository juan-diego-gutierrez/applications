package co.com.pragma.api;

import co.com.pragma.api.dto.ApplicationDTO;
import co.com.pragma.api.dto.SuccessResponse;
import co.com.pragma.api.exception.RequestValidator;
import co.com.pragma.api.mapper.ApplicationMapper;
import co.com.pragma.usecase.application.ApplicationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.time.LocalDateTime;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
  public Mono<ServerResponse> saveApplication(ServerRequest serverRequest) {
    logger.info("Received request to save application");

    return serverRequest.bodyToMono(ApplicationDTO.class)
        .flatMap(requestValidator::validate)
        .flatMap(createApplicationDTO -> applicationUseCase.saveApplication(
            applicationMapper.toApplication(createApplicationDTO)))
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
