package co.com.pragma.api;

import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

  private static final String APPLICATIONS_PATH = "/api/v1/applications";

  @Bean
  @RouterOperations({
      @RouterOperation(path = APPLICATIONS_PATH, method = RequestMethod.POST, beanClass = ApplicationHandler.class, beanMethod = "saveApplication",
          operation = @Operation(summary = "Save a new application", description = "This endpoint allows you to save a new application.")),
      @RouterOperation(path = APPLICATIONS_PATH + "/getAll", method = RequestMethod.POST, beanClass = ApplicationHandler.class, beanMethod = "getAllApplications",
          operation = @Operation(summary = "Get applications", description = "This endpoint allows you to get applications and filter by status and application type."))
  })
  public RouterFunction<ServerResponse> applicationRoutes(ApplicationHandler applicationHandler) {
    return route()
        .POST(APPLICATIONS_PATH, applicationHandler::saveApplication)
        .POST(APPLICATIONS_PATH + "/getAll", applicationHandler::getAllApplications)
        .build();
  }
}
