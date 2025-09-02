package co.com.pragma.r2dbc.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table("application_types")
public class ApplicationTypeEntity {
  @Id
  @Column("id_application_type")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  @Column("min_amount")
  private BigDecimal minAmount;
  @Column("max_amount")
  private BigDecimal maxAmount;
  @Column("interest_rate")
  private BigDecimal interestRate;
  @Column("automatic_validation")
  private boolean automaticValidation;
}
