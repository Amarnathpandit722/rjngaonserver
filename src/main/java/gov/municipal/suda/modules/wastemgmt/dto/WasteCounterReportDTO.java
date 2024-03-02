package gov.municipal.suda.modules.wastemgmt.dto;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WasteCounterReportDTO {
    private String holding_no;
    private BigInteger ward_no;
    private String consumer_no;
    private String consumer_name;
    private BigInteger mobile;
    private String frm_month;
    private String to_month;
    private String transaction_date;
    private String transaction_no;
    private String payment_mode;
    private String cheque_no;
    private String cheque_date;
    private String bank_name;
    private String branch_name;
    private BigDecimal amount;
    private String tax_collector;
}
