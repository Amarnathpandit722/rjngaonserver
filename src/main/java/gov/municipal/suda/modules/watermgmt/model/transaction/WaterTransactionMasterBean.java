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
@Table(name="tbl_wtr_transaction_mstr",schema = "watermgmt", catalog = "")
public class WaterTransactionMasterBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long ward_id;
    private Long consumer_dets_id;
    private Long conn_type_id;
    private Long property_type_id;
    private String transaction_no;
    private String transaction_date;
    private BigDecimal demand_amount;
    private BigDecimal penalty;
    private BigDecimal payable_amount;
    private String remarks;
    private String ip_address;
    private String payment_mode;
    private Integer cancel_status;
    private Integer cash_verify_status;
    private Long cash_verify_by;
    private String verify_date;
    private String entry_date;
    private String entry_time;
    private Long user_id;
    private BigDecimal reg_amt;
    private Long old_ward_id;
}
