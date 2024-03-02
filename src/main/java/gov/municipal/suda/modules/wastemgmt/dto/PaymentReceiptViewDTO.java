package gov.municipal.suda.modules.wastemgmt.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PaymentReceiptViewDTO {
    private String transaction_no;
    private String transaction_date;
    private Long ward_no;
    private String address;
    private String department_section;
    private String account_description;
    private String consumer_no;
    private String holding_no;
    private String consumer_name;
    private String guardian_name;
    private Long mobile_no;
    private String payment_mode;
    private String periods;
    private String cheque_no;
    private String cheque_date;
    private String bank_name;
    private String branch_name;
    private BigDecimal payable_amount;
    private String from_yr_mnth;
    private String to_yr_mnth;

}
