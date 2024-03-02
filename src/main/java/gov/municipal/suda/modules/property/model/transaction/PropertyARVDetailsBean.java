package gov.municipal.suda.modules.property.model.transaction;

import gov.municipal.suda.modules.property.model.master.PropertyMasterBean;
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
@Table(name="tbl_prop_arv_detail")
public class PropertyARVDetailsBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="prop_id",nullable=false)
    private Long prop_id;
    private Long zone_id;
    private Long usage_type_id;
    private String occupancy_type;
    private String construction_type;
    private String floor_name;
    private String arv;
    private String widow_case;
    private String phys_disable;
    private String house_maint_arv;
    private String old_prop_five_per_arv;
    private String calc_arv;
    private String effect_year;
    private String built_up_area;
    private String completion_date;
    private String comupto_date;
    private Long arv_rate_mstr_id;
    private BigDecimal arv_rate;
    private BigDecimal flr_dis_rate;
    private BigDecimal f_calc_rate;
    private String code;
    private String table_name;
    private Integer status;
    private Timestamp stampdate;
    private Long entry_fy_id;
    private String fy_year_date;
    private String fy_end_date;


//    @OneToOne
//    @PrimaryKeyJoinColumn(name = "prop_id")
//    SAFARVDetailsBean safarvDetailsBean;

    @ManyToOne(optional = false)
    @JoinColumn(name="prop_id", referencedColumnName="id", insertable = false, updatable = false)
    private PropertyMasterBean propertyMaster;
}
