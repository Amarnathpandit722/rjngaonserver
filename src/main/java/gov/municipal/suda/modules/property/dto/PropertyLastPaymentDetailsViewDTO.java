package gov.municipal.suda.modules.property.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PropertyLastPaymentDetailsViewDTO {
    private String year;
    private String dueDate;
    private BigDecimal amount;
    private BigDecimal penalty;
}
