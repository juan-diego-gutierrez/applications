package co.com.pragma.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ApplicationDTO(
    @NotNull(message = "Amount cannot be empty")
    BigDecimal amount,
    @NotNull(message = "Term cannot be empty")
    int term,
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email must have a valid format")
    String email,
    @NotNull(message = "Application type ID cannot be empty")
    Long applicationTypeId,
    Long statusId
) {

}
