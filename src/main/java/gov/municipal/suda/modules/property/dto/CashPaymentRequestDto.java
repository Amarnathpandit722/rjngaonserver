package gov.municipal.suda.modules.property.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CashPaymentRequestDto {
    private String property_no;
    private List<String> effective_date;  // set
    private List<Long> id;
    private String payment_mode; // get the name from drop down of mode of payment
    private Integer payment_mode_id; // get this id from Drop down of mode of payment
    private Long user_id;
    private String ip_address;
    private String narration;
    private String due_from_year;
    private String due_up_to_year;
    private String bank_name;
    private String others_bank_name;
    private String branch;
    private String cheque_no;
    private String cheque_date;
    private String card_type;
    private Long transaction_id;
    private Long appr_code;
    private Integer card_no;
    private String card_holder_name;
    private String neft_no;
    private String neft_date;
    private BigDecimal payable_amt;
    private BigDecimal penalty;
    private BigDecimal discount;
    private BigDecimal demand_payment;
    private BigDecimal form_fee;
    private BigDecimal rain_harvesting;
    private BigDecimal difference_amount;
    private String dd_no;
    private String dd_date;
    private String rtgs_no;
    private String rtgs_date;
    private String demand_no;
}
