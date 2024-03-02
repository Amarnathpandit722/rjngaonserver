package gov.municipal.suda.modules.property.model.transaction;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_prop_tranction_mstr")
public class PropertyTransactionBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long ward_id;
    private Long prop_id;
    private Long usage_type_id;
    private String transaction_no;
    private Timestamp transaction_date;
    private BigDecimal payable_amt;
    private BigDecimal penalty;
    private BigDecimal discount;
    private BigDecimal demand_payment;
    private BigDecimal form_fee;
    private String remarks;
    private String payment_mode;
    private String ip_address;
    private Integer cancel_status; //default 0 =false
    private Integer cash_verify_stts; //default 0 =false
    private Long cash_verify_id; // default null
    private Timestamp verify_date; // default null
    private Timestamp stampdate;
    private Long user_id;
    private Integer status;
    private BigDecimal rain_wtr_harvest;
    private Long old_ward_id;
    private BigDecimal difference_amount;
}
