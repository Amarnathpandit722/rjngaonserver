package gov.municipal.suda.modules.wastemgmt.model.transaction;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_demand_log",schema = "wastemgmt", catalog = "")
public class UserChargeDemandLogBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long consumer_detail_id;
    private BigDecimal total_amt;
    private Long user_id;
    private String stampdate; //format yyyy-mm-dd
    private String entry_time; //format hh:mm:ss
    private String fromdate; // format yyyy-mm-dd
    private String todate; // format yyyy-mm-dd

}
