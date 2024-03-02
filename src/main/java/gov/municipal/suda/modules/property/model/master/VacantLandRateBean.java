package gov.municipal.suda.modules.property.model.master;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_vacantland_rate")
public class VacantLandRateBean {
    @Id
    @Generated
    private Long id;
    private String municipal_type;
    private Long road_id;
    private Long zone_id;
    private BigDecimal rate;
    private String doe;
    private Long uses_type_id;
    private String status;
}
