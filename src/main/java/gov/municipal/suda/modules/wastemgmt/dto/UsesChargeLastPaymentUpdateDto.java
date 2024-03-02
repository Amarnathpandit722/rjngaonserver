package gov.municipal.suda.modules.wastemgmt.dto;

import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UsesChargeLastPaymentUpdateDto {
    private String month;
    private BigDecimal amount;
}
