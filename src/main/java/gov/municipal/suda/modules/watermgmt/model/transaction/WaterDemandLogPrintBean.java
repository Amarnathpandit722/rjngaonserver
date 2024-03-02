package gov.municipal.suda.modules.watermgmt.model.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_wtr_demand_print_log",schema = "watermgmt", catalog = "")
public class WaterDemandLogPrintBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long ward_id;
    private Long consumer_dets_id;
    private String demand_from;
    private String demand_upto;
    private BigDecimal tot_amount;
    private String entry_date;
    private String entry_time;
    private Long user_id;
    private Long old_ward_id;
}
