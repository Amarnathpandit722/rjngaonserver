package gov.municipal.suda.modules.wastemgmt.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyRateViewDTO {
    private BigDecimal monthly_rate;
    private String doe;
}
