package gov.municipal.suda.modules.wastemgmt.model.master;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_consumer_master",schema = "wastemgmt", catalog = "")
public class ConsumerMasterBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Timestamp stampdate;
    private String  holding_no;
    private String  ward_id;
    private String  police_station;
    private String land_mark;
    private String  address;
    private Long created_byid;
    private Integer status;
    private Long ward_no;
    private String shop_consumer_no;
    private Long old_ward_id;
    private Long old_ward_no;
    private String uploaded_doc;
    private Long update_ward_user_id;
    private Long update_status_ward;
}
