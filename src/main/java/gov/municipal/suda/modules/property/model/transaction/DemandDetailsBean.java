package gov.municipal.suda.modules.property.model.transaction;

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
@Table(name="tbl_prop_demand_dtls")
public class DemandDetailsBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Auto generated
    private Long ward_id; // get it from tbl_property_mstr table
    private Long prop_id; // get it from tbl_property_mstr table
    private String demand_no; // same value  of above id field, current difference between id field and demand no field is 20
    private String demand_date; // demand creation date format (YYYY-MM-DD)
    private BigDecimal property_tax; // calculate on total buildup field area from tbl_property_mstr table
    private BigDecimal sanitation_tax;
    private BigDecimal composite_tax ;
    private BigDecimal common_wtr_tax;
    private BigDecimal personal_wtr_tax ;
    private BigDecimal education_cess;
    private BigDecimal penalty;
    private BigDecimal penal_charge;
    private BigDecimal otheramt;
    private BigDecimal total_amount; // formula is (property_tax+sanitation_tax+composite_tax+common_wtr_tax+personal_wtr_tax+education_cess+penalty+penal_charge+otheramt
    private BigDecimal aproperty_tax;
    private BigDecimal asanitation_tax;
    private BigDecimal acomposite_tax;
    private BigDecimal acommon_wtr_tax;
    private BigDecimal apersonal_wtr_tax;
    private BigDecimal aeducation_cess;
    private BigDecimal apenal_charge;
    private BigDecimal atotal_amount;
    private BigDecimal arain_harvest_charge;
    private Long fy_id;  //Financial year id get from tbl_fy table
    private String effect_year; // choose from Drop-down list (eg: 2023-2024)
    private Integer paid_status; // by default false(0) during demand creation, when payment has made than it's become 1 true.
    private Long tax_rate_id; // tbl_prop_owner_tax_mstr id is the tax_rate_id
    private Long last_payment_id; // get from tbl_prop_last_payment_record table
    private String demand_type; // it is an entry type (New Assessment, Re-assessment etc), get it from tbl_property_master
    private Integer demand_deactive; // Need to ask when demand get deactivated, by default it is false (0)
    private Timestamp stampdate; // current date and time
    private Long user_id;
    private Integer status;
    private Long correction_status; // If any correction required than true (1) by default false (0)
    private Long entry_fy_id; // get it from tbl_property_mstr table entry_fy_id
    private BigDecimal rain_harvest_charge; // first check in tbl_property_mstr table rain_harvest field Y, if Y than make charge from tbl_rain_harvest_mstr on total buildtup area
    private Long  old_ward_id;
    private Integer diff_sts; // need to discuss but now default value is 0, for difference Amount status
    private BigDecimal othCommWtrTax;
    private BigDecimal othCompTax;
    private BigDecimal othEduCess;
    private BigDecimal othPenalCharge;
    private BigDecimal othPenalty;
    private BigDecimal othPeronalWtrTax;
    private BigDecimal othPropTax;
    private BigDecimal othRainHarvChrg;
    private BigDecimal othSanitTax;
    private BigDecimal othTotAmount;
    private BigDecimal finalAmountAfterDiscount;
    private BigDecimal allowance_percent;

}
