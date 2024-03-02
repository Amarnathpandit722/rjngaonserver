package gov.municipal.suda.modules.watermgmt.dto.transaction;

import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WaterLastPaymentViewResponseDto {
    private String demandFrom;
    private String demandTo;
    private BigDecimal amount;
}
