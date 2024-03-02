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
@Table(name = "tbl_saf_edu_composit_rate_mstr")
public class EduAndCompositeRateBean {

    @Id
    @Generated
    private Long id;
    private BigDecimal edu_percentage;
    private BigDecimal notpayee_edu_percentage;
    private BigDecimal payee_composit_amt;
    private String doe;
    private BigDecimal notpayee_composit_amt;
    private BigDecimal common_water;
    private BigDecimal notpayee_common_water;
    private BigDecimal personal_water;

}
