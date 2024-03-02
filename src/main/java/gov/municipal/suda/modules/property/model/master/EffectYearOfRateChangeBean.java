package gov.municipal.suda.modules.property.model.master;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_ratechange_effect")
public class EffectYearOfRateChangeBean {
    @Id
    @Generated
    private Long id;
    private String effect_date;
    private String effect_year;
    private Long effect_year_id;
    private String status;
    private Long user_id;
    private Timestamp stampdate;

}
