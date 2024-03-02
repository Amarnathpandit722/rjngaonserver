package gov.municipal.suda.modules.wastemgmt.model.master;

import lombok.*;


import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_consumer_rate_chart",schema = "wastemgmt", catalog = "",uniqueConstraints = @UniqueConstraint(columnNames = {"consumer_range_mstr_id"}))
public class ConsumerRateChartBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long consumer_range_mstr_id;
    private BigDecimal amount;
    private String fee_effectdate;
    private Integer status;

    @ManyToOne
    @JoinColumn(name="consumer_range_mstr_id", referencedColumnName="id",
            insertable =  false, updatable = false)
    private  ConsumerRangeMasterBean range;
   // private BigDecimal per_restuarant;
    //private BigDecimal per_garden;
    //private BigDecimal per_banquethall;
}
