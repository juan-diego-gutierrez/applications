package co.com.pragma.usecase.application.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
  TYPE_NOT_FOUND("Application type not found"),
  STATUS_NOT_FOUND("Status not found"),
  USER_NOT_FOUND("User not found");

  private final String message;

  ErrorCode(String message) {
    this.message = message;
  }

  public String getCode() {
    return this.name();
  }

}

