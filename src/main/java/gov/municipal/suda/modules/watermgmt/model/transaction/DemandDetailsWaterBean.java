package gov.municipal.suda.modules.watermgmt.model.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_consumer_demand_dtls",schema = "watermgmt", catalog = "")
public class DemandDetailsWaterBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long ward_id;
    private Long consumer_dets_id;
    private String demand_date;
    private String demand_from;
    @JsonFormat(pattern="yyyy-MM-dd")
    private String demand_upto;
    private Long unit_rate_id;
    private BigDecimal unit_amount;
    private BigDecimal extra_room_charge;
    private BigDecimal penalty;
    private BigDecimal demand_amount;
    private BigDecimal late_fine;
    private Long fy_id;
    private String fy_year;
    private Long current_meter_reading;
    private Integer payment_status;
    private Long last_payment_id;
    private Long user_id;
    private String entry_date;
    private String entry_time;
    private Integer status;
    private Integer tad_update;
    private String image_upl;
    private BigDecimal balance;
    private Long old_ward_id;
    

}
