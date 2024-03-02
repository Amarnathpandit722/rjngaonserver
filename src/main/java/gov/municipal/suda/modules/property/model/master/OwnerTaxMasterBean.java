package gov.municipal.suda.modules.property.model.master;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_prop_owner_tax_mstr")
@ToString
public class OwnerTaxMasterBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long prop_id;
    private BigDecimal arv;
    private BigDecimal beforeproperty_tax;
    private Long comm_composit_per_id;
    private BigDecimal composite_taxper_rangeamt;
    private BigDecimal samanyajalkarper_rangeamt;
    private BigDecimal property_tax;
    private BigDecimal sanitation_tax;
    private BigDecimal composite_tax;
    private BigDecimal common_wtr_tax;
    private BigDecimal personal_wtr_tax;
    private BigDecimal education_cess;
    private BigDecimal otheramt;
    private BigDecimal tot_yearly_tax;
    private Long fy_id;
    private String effect_year;
    private Timestamp stampdate;
    private Long user_id;
    private Integer status;
    private Long entry_fy_id;
}
