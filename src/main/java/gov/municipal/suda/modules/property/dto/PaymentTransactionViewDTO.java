package gov.municipal.suda.modules.property.dto;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_reconcillation_view")
public class PaymentTransactionViewDTO {
    @Id
    private BigInteger id;
    private BigInteger prop_id;
    private String ward_name;
    private  String property_no;
    private  String owner_name;
    private String transaction_date;
    private String transaction_no;
    private String payment_mode;
    private BigDecimal payable_amt;
    private String tax_collector;
    private Timestamp stampdate;
    private String status;
    private String cancel_status;
    private String check_status;
    private String cash_verify_stts;

}
