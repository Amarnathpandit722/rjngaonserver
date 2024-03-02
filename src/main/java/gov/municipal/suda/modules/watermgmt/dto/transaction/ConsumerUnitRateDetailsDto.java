package gov.municipal.suda.modules.watermgmt.dto.transaction;

import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerUnitRateDetailsDto {
    private String effectFrom;
    private BigDecimal unitRate;
    private BigDecimal late_fine;
    private BigDecimal extraCharge;
    private String status;


}
