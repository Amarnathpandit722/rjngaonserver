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
@Table(name="tbl_transaction_master",schema = "wastemgmt", catalog = "")
public class ConsumerTransactionBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private Long consumer_detail_id;
    private Long ward_id;
    private String transaction_no;
    private String transaction_date;
    private BigDecimal payable_amt;
    private BigDecimal penalty;
    private BigDecimal discount;
    private BigDecimal demand_amt;
    private String remarks;
    private String payment_mode;
    private Long cancel_status;
    private Long cash_verify_id;
    private String cash_verify_date;
    private Long cash_verify_status;
    private String ip_address;
    private Integer user_id;
    private Timestamp stampdate;
    private Integer status;
    private Long old_ward_id;
}
