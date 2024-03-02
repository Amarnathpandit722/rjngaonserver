package gov.municipal.suda.modules.property.model.transaction;

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
@Table(name="tbl_prop_notice")
public class PropertyNoticeBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long notice_no ;
    private Long prop_id;
    private Long ward_id;
    private String ward_no;
    private String holding_no;
    private String demand_from_yr;
    private String demand_upto_yr;
    private BigDecimal amount;
    private Long dmd_from_fyid;
    private Long dmd_upto_fyid;
    private String user_frm_mnth;
    private String user_upto_mnth;
    private BigDecimal user_amount;
    private Long user_id;
    private BigDecimal first_notice_days ;
    private BigDecimal second_notice_days;
    private String first_notice_date ;
    private String second_notice_date;
    private Long second_user_id;
    private Timestamp stampdate;
    private Integer status;
    private Integer status_175;
    private String notice_date_175 ;
    private Long user_id_175 ;
    private Long old_ward_id ;
    private Long old_ward_no;
    private Integer arr_stutus; // arrear status
}
