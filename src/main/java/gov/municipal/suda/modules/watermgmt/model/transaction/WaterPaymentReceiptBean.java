package gov.municipal.suda.modules.watermgmt.model.transaction;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_wtr_payment_receipt",schema = "watermgmt", catalog = "")
public class WaterPaymentReceiptBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long transaction_id;
    private Long consumer_dets_id;
    private String curr_due_from;
    private String curr_due_upto;
    private Long due_months;
    private BigDecimal curr_tot_amount;
    private BigDecimal prev_arrear;
    private BigDecimal penalty;
    private BigDecimal total_billing_amount;
    private BigDecimal adjust_amount;
    private BigDecimal arr_adjust_amount;
    private BigDecimal amount_paid;
    private BigDecimal total_due;
    private BigDecimal advance_amount;
    private String curr_meter_reading;
    private String prev_meter_reading;
    private String entry_date;
    private String entry_time;
    private Long user_id;
    private BigDecimal reg_amt;
}
