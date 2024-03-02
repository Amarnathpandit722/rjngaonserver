package gov.municipal.suda.modules.property.model.master;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_prop_last_payment_record")
public class LastPaymentRecordBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long prop_id;
    private String receipt_no;
    private String receipt_date;
    private String book_no;
    private String recpt_filename;
    private Long user_id;
    private String entry_date;
    private String time;
    private String frm_year;
    private String upto_year;
    private BigDecimal tot_amount;
    private BigDecimal fine_amount;
}
