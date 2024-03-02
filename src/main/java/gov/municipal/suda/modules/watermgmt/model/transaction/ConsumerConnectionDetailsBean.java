package gov.municipal.suda.modules.watermgmt.model.transaction;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_consumer_conn_dtls",schema = "watermgmt", catalog = "")
public class ConsumerConnectionDetailsBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long ward_id;
    private Long property_type_id;
    private Long conn_type_id;
    private Long consumer_dets_id;
    private Long rate_mstr_id;
    private Long no_table_room;
    private String date_of_connection;
    private String meter_no;
    private Long intial_meter_reading;
    private Long user_id;
    private String entry_date;
    private String entry_time;
    private Integer status;
    private String meter_reading_in;
    private String declaration_doc;
    private String nigam_emp_doc;
    private String reg_doc;
    private Long old_ward_id;
}
