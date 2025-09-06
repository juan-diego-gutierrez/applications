package co.com.pragma.r2dbc.entity;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDataEntity {

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
