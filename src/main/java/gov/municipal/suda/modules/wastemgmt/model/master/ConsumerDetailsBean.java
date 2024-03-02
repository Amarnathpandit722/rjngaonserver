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
@Table(name="tbl_consumer_details",schema = "wastemgmt", catalog = "")
public class ConsumerDetailsBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long consumer_mstr_id;
    private String consumer_no;
    private String consumer_name;
    private Long mobile_no;
    private String house_flat_no;
    private String consumer_type;
    private Long consumer_details_id;
    private Long created_byid;
    private Integer status;
    private Timestamp stampdate;
    private String gradian_name;
    private String relation;
    private String doc_file;
    private Timestamp upadte_time;
    private Long upadte_user_id;
    private String upadte_type;
}
