package gov.municipal.suda.modules.watermgmt.model.master;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_consumer_dtls",schema = "watermgmt", catalog = "")
public class WaterConsumerDetailsBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long ward_id;
    private Long prop_type_id;
    private String old_consumer_no;
    private String consumer_no;
    private String holding_no;
    private String property_address;
    private String is_nigam_emp;
    private String entry_date;
    private String entry_time;
    private Long user_id;
    private Integer status;
    private String regularization;
    private Long old_ward_id;
    private String uploaded_doc;
    private Long  update_ward_user_id;
    private Long update_status_ward;
}
