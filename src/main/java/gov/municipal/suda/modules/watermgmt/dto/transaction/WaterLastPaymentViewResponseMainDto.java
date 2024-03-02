package gov.municipal.suda.modules.watermgmt.dto.transaction;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WaterLastPaymentViewResponseMainDto {
    private BigDecimal totalPayment;
    private Long consumerDetailsId;
    private List<WaterLastPaymentViewResponseDto> lastPayment;
}
