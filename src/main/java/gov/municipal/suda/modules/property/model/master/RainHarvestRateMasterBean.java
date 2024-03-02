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
@Table(name="tbl_rain_harvest_mstr")
public class RainHarvestRateMasterBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal buildup_area_from;
    private BigDecimal buildup_area_upto;
    private BigDecimal rain_harvest_charge;
    private Long user_id;
    private Integer status;
    private String doe;
}
