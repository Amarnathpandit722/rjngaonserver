package gov.municipal.suda.modules.property.model.master;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_old_building")
public class OldBuildingRatePercentageBean {
    @Id
    @Generated
    private Long id;
    private Integer count_year_from;
    private Integer count_year_to;
    private BigDecimal percentage;
}
