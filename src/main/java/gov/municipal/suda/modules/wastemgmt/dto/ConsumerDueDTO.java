package gov.municipal.suda.modules.wastemgmt.dto;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerDueDTO {

    private Long demand_id;
    private BigDecimal demand_amount;
    private String demand_from;
    private String demand_to;
}
