package gov.municipal.suda.modules.property.dto;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_transaction_view")
public class TransactionViewUpdateDTO {
    @Id
    private Long id;
    private String property_no;
    private String owner_name;
    private String owner_address;
    private String ward_name;
    private String transaction_no;
    private BigDecimal payable_amt;
    private String transaction_by;
    private String transaction_date;
    private String payment_mode;
    private String bank_name;
    private String branch_name;
    private String cheque_no;
    private String cheque_dt;
    private String card_no;
    private String card_type;
    private String card_holder_name;
    private Long prop_id;
    private String transaction_mode;
    private Integer status;
    private Integer cancel_status;
    private Integer cash_verify_stts;
    private Integer cheque_status;

}
