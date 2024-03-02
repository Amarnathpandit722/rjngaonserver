package gov.municipal.suda.modules.watermgmt.model.master;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_consumer_basic_dtls",schema = "watermgmt", catalog = "")
public class ConsumerBasicDetailsBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long consumer_dets_id;
    private String name;
    private String relation;
    private String guardian_name;
    private Long mobile_no;
    private String entry_date;
    private String entry_time;
    private Long user_id;
    private Integer status;
    private Long bpl_apl_id;
}
