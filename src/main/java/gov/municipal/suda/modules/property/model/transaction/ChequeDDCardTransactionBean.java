package gov.municipal.suda.modules.property.model.transaction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name="tbl_prop_transaction_dtls")
public class ChequeDDCardTransactionBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long prop_id;
    private Long transaction_id;
    private String cheque_no;
    private String cheque_dt;
    private String bank_name;
    private String branch_name;
    private Integer card_no;
    private String card_holder_name;
    private String card_type;
    private BigDecimal amount;
    private Integer check_status;
    private Timestamp reconcilation_dt;
    private Long cleared_by;
    private Timestamp clear_stampdate;
    private String remarks;
    private String chq_img;
    private String rrn_no;
    private String txn_ref_id;
    private String upi_trans_id;

}
