package gov.municipal.suda.modules.wastemgmt.model.transaction;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_transaction_details",schema = "wastemgmt", catalog = "")
public class ConsumerTransactionDetailBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private Long transcation_mstr_id;
    private Long consumer_detail_id;
    private String cheque_no;
    private String cheque_date;
    private String branch_name;
    private String bank_name;
    private BigDecimal amount;
    private Integer cheque_status;
    private String reconcile_date;
    private Integer cleared_by;
    private String cleared_stampdate;
    private String reason;
    private String card_type;
    private String card_holder_name;
    private String card_no;
}
