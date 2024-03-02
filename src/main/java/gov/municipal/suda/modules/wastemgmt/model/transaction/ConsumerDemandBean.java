package gov.municipal.suda.modules.wastemgmt.model.transaction;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_consumer_demand",schema = "wastemgmt", catalog = "")
public class ConsumerDemandBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long consumer_detail_id;
    private Long ward_id;
    private String demand_from; //YYYY-MM-DD (2016-10-30)
    private String demand_to;   //YYYY-MM-DD (2016-10-30)
    private Long rate_chart_id;
    private BigDecimal demand_amount;
    private Long financial_year_id;
    private String financial_year;
    private Long payment_status;
    private BigDecimal user_id;
    private String stampdate;
    private Integer status;
    private Long last_payment_id;
    private Long old_ward_id;
}
