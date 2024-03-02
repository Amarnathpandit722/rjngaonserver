package gov.municipal.suda.modules.watermgmt.model.master;


import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="wtr_meter_rate_chart",schema = "watermgmt", catalog = "")
public class WaterMeterRateChartBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long prop_type_id;
    private Long range_from;
    private Long range_upto;
    private BigDecimal amount;
    private String date_of_effect;
    private String entry_date;
    private String entry_time;
    private Long user_id;
    private Integer status;
}
