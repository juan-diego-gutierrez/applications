package co.com.pragma.api.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ApplicationTypeDTO(
    @NotNull(message = "Application type ID cannot be empty")
    Long id,
    String name,
    BigDecimal minAmount,
    BigDecimal maxAmount,
    BigDecimal interestRate,
    boolean automaticValidation
) {

}
