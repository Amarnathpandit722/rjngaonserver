package gov.municipal.suda.modules.wastemgmt.model.transaction;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_last_payment_record",schema = "wastemgmt", catalog = "")
public class ConsumerLastPaymentBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long consumer_details_id;
    private String receipt_no;
    private String receipt_date; //YYYY-MM-DD (2017-04-10)
    private String book_no;
    private String recpt_filename;
    private String frm_month; //YYYY-MM-DD (2016-10-30)
    private String upto_month; //MM-YYYY (10-2016)
    private BigDecimal tot_amount;
    private Timestamp stampdate;
    private Long user_id;
    private Integer status;
}
