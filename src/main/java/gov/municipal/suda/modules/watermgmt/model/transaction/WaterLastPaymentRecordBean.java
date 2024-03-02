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
@Table(name="tbl_wtr_last_payment_update_dtls",schema = "watermgmt", catalog = "")
public class WaterLastPaymentRecordBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long consumer_dets_id;
    private String receipt_no;
    private String receipt_date;
    private String book_no;
    private String recpt_filename;
    private String due_month_from;
    private String due_month_upto;
    private BigDecimal tot_amount;
    private String entry_date;
    private String entry_time;
    private Long user_id;
}
