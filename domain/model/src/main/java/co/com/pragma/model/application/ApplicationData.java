package co.com.pragma.model.application;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ApplicationData {

  private BigDecimal amount;
  private int term;
  private String email;
  private String name;
  private String applicationType;
  private BigDecimal interestRate;
  private String status;
  private BigDecimal baseSalary;
  private BigDecimal totalMonthlyDebt;
}
