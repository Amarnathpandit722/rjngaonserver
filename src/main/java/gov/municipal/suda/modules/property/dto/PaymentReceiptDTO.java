package gov.municipal.suda.modules.property.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PaymentReceiptDTO {
    private Long id;
    private Long prop_id;
    private String receipt_no; //not require transform transactionNo
    private String receipt_date; // not require instead of transaction date
    private String book_no; // not require
    private String recpt_filename; // not require
    private Long user_id;
    private String entry_date;
    private String time;
    private String frm_year;
    private String upto_year;
    private BigDecimal tot_amount;
    private BigDecimal fine_amount;
    private String transaction_no;
    private String payment_mode;
    private Long check_status;

}
