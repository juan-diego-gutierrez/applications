package co.com.pragma.model.application;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Application {

  private BigDecimal amount;
  private int term;
  private String email;
  private Long applicationTypeId;
  private Long statusId;
}
