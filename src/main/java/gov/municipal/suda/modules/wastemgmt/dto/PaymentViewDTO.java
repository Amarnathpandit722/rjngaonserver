package gov.municipal.suda.modules.wastemgmt.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PaymentViewDTO {
    private String transaction_no;
    private String transaction_date;
    private String from_month;
    private String to_month;
    private String payment_mode;
    private BigDecimal tot_amount;
    private String collected_by;
}
