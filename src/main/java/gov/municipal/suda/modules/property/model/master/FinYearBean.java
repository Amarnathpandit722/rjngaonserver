package gov.municipal.suda.modules.property.model.master;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigInteger;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_fy")
public class FinYearBean {
    @Id
    @Generated
    private Long id;
    private String fy_name;
    private String status;

    @Transient
    private BigInteger yr_id;
    @Transient
    private String effective_date;
}
