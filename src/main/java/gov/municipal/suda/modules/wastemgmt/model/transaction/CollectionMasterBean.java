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
@Table(name="tbl_collection_master",schema = "wastemgmt", catalog = "")
public class CollectionMasterBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private Long ward_id;
    private Long consumer_detail_id;
    private Long demand_details_id;
    private Long transaction_mstr_id;
    private BigDecimal total_amt;
    private Long fy_id;
    private Integer month;
    private String year;
    private Long user_id;
    private Timestamp stampdate;
    private Long old_ward_id;
    private Integer status;
}
