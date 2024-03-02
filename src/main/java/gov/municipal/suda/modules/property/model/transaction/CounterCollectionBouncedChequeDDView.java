package gov.municipal.suda.modules.property.model.transaction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name="counter_collection_report_view_for_bounce_cheque_dd")
public class CounterCollectionBouncedChequeDDView {

    @Id
    private Long ward_id;
    private BigDecimal acomposite_tax;
    private BigDecimal aproperty_tax;
    private String bank_name;
    private String branch_name;
    private String cheque_dd_aprcode;
    private BigDecimal composite_tax;
    private BigDecimal discount;
    private BigDecimal education_cess;
    private BigDecimal aeducation_cess;
    private BigDecimal form_fee;
    private Long mobile_no;
    private String owner_name;
    private String payment_mode;
    private BigDecimal penal_charge;
    private BigDecimal penalty;
    private String property_no;
    private BigDecimal property_tax;
    private BigDecimal rain_harvest_charge;
    private String stampdate;
    private String tax_collector;
    private BigDecimal tot_amount;
    private String transaction_date;
    private String transaction_no;
    private Long user_id;
    private String ward_name;
    private  String for_year;

}
