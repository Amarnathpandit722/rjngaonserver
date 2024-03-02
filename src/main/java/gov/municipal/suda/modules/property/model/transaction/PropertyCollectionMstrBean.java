package gov.municipal.suda.modules.property.model.transaction;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tbl_prop_collection_mstr")
public class PropertyCollectionMstrBean implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long ward_id;
    private Long prop_id;
    private Long demand_id;
    private Long transaction_id;
    private BigDecimal property_tax;
    private BigDecimal sanitation_tax;
    private BigDecimal composite_tax;
    private BigDecimal common_wtr_tax;
    private BigDecimal personel_wtr_tax;
    private BigDecimal education_cess;
    private BigDecimal penalty;
    private BigDecimal penal_charge;
    private BigDecimal otheramt;
    private BigDecimal tot_amount;
    private BigDecimal aproperty_tax;
    private BigDecimal asanitation_tax;
    private BigDecimal acomposite_tax;
    private BigDecimal acommon_wtr_tax;
    private BigDecimal apersonal_wtr_tax;
    private BigDecimal aeducation_cess;
    private BigDecimal apenal_charge;
    private BigDecimal atotal_amount;
    private Long fy_id;
    private String for_year;
    private Timestamp stampdate;
    private Long user_id;
    private int status;
    private BigDecimal rain_harvest_charge;
    private BigDecimal arain_harvest_charge;
    private Long old_ward_id;
}
