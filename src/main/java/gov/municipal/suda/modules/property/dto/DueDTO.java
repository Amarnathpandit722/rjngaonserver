package gov.municipal.suda.modules.property.dto;

import lombok.*;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DueDTO {
    private BigDecimal total_amount;
    private BigDecimal penalty;
    private BigDecimal penal_charge;
}
