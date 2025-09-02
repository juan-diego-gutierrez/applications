package co.com.pragma.api.exception;

import java.util.Map;
import lombok.Getter;

@Getter
public class InvalidRequestException extends RuntimeException {

  private final Map<String, String> errors;

  public InvalidRequestException(Map<String, String> errors) {
    super("Validation failed");
    this.errors = errors;
  }
}
