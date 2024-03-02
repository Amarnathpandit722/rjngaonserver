package gov.municipal.suda.modules.wastemgmt.dto;

import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerDemandViewDTO {
    private Long id;
    private String demand_from;
    private String demand_to;
    private BigDecimal demand_amount;
}
