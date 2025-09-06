package co.com.pragma.api.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ApplicationFilterDTO {

  private String status;
  @Positive
  private Long applicationTypeId;
  @PositiveOrZero
  private int page = 0;
  @Positive
  private int size = 10;
}
