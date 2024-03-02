package gov.municipal.suda.modules.property.dto;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DemandViewDTO {

    private BigInteger prop_id;
    private BigDecimal total_amount;
    private BigDecimal penalty;
    private String effect_year;
    private String demand_date;
}