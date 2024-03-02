package gov.municipal.suda.modules.property.dto;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_prop_owner_tax_view")
public class OwnerTaxViewDTO {
    @Id
    private Long prop_id;
    private BigDecimal property_tax;
    private BigDecimal sanitation_tax;
    private BigDecimal composite_tax;
    private BigDecimal common_wtr_tax;
    private BigDecimal personal_wtr_tax;
    private BigDecimal education_cess;
    private BigDecimal otheramt;
    private BigDecimal tot_yearly_tax;
    private String effect_year;
}
