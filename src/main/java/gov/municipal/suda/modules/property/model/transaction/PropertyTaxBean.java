package gov.municipal.suda.modules.property.model.transaction;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_prop_owner_tax_mstr", schema = "public")
public class PropertyTaxBean implements Serializable {

    @Id
    @Generated
    private Long id;
    private Long prop_id;
    private Integer arv;
    private Double beforeproperty_tax;
    private Long comm_composit_per_id;
    private Double composite_taxper_rangeamt;
    private Double samanyajalkarper_rangeamt;
    private Double property_tax;
    private Double sanitation_tax;
    private Double composite_tax;
    private Double common_wtr_tax;
    private Double personal_wtr_tax;
    private Double education_cess;
    private Double otheramt;
    private Double tot_yearly_tax;
    private Long fy_id;
    private String effect_year;
    private OffsetDateTime stampdate;
    private Long user_id;
    private Integer status;
    private Long entry_fy_id;
}
