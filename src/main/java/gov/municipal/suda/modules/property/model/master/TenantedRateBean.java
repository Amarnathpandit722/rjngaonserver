package gov.municipal.suda.modules.property.model.master;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_prop_tenant_rate")
public class TenantedRateBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long range_from;
    private Long range_to;
    private String effective_year;
    private BigDecimal percentage;
    private Integer status;
    private Long occup_type_id;
   }
