package gov.municipal.suda.modules.property.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentViewBeforeTransDTO {
    private List<Long> id;
    private BigDecimal payable_amt;
    private BigDecimal penalty;
    private BigDecimal discount;
    private BigDecimal demand_payment;
    private BigDecimal form_fee;
//    private List<String> effective_date;
//    private String from_year;
//    private String to_year;
}
