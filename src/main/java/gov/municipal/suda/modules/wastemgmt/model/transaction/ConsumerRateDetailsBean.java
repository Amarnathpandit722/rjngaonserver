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
@Table(name="tbl_consumer_rate_details",schema = "wastemgmt", catalog = "")
public class ConsumerRateDetailsBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long consumer_details_id;
    private Long consumer_cat_mstr_id;
    private Long consumer_range_mstr_id;
    private Long noof_restaurant;
    private Long noof_garden;
    private Long noof_banquethall;
    private BigDecimal noof_sqft_truck_room;
    private Long financial_year_id;
    private Long month;
    private String financial_year;
    private Integer status;
    private String doe;
    private Long consumer_rate_mstr_id;
    private Timestamp stampdatetime;
}
