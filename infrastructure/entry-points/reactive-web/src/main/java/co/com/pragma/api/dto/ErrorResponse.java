package co.com.pragma.api.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
    String status,
    Map<String, String> errors,
    String message,
    LocalDateTime timestamp,
    String path) {

}
