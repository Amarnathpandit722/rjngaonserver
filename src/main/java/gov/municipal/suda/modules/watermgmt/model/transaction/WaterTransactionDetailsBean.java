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
@Table(name="tbl_wtr_transaction_dtls",schema = "watermgmt", catalog = "")
public class WaterTransactionDetailsBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long transaction_id;
    private Long consumer_dets_id;
    private String chq_dd_no;
    private String chq_dd_dte;
    private String bank_name;
    private String branch_name;
    private BigDecimal chk_dd_amount;
    private Long card_no;
    private String card_holder_name;
    private String card_type;
    private Integer chk_dd_clear_status;
    private String recancilation_date;
    private Long cleared_by;
    private String remarks;
    private String clear_entry_date;
    private String entry_time;
    private Long user_id;
}
