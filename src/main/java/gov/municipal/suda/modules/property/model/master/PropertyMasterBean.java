package gov.municipal.suda.modules.property.model.master;

import gov.municipal.suda.modules.property.model.transaction.SAFARVDetailsBean;
import gov.municipal.suda.modules.property.model.transaction.SAFEntryPK;
import gov.municipal.suda.util.enumtype.BuildingType;
import lombok.*;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_property_mstr")

public class PropertyMasterBean implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  Long id;
    private  Long ward_id;
    private  String entry_type;
    @Column(unique=true)
    private  String property_no;
    private  Long property_type_id;
    private  Long road_type_id;
    private  Long area_id;
    private  String mohalla;
    private  String property_address;
    private  String city;
    private  String district;
    private  String vsrno;
    private  String pincode;
    private BigDecimal totalbuilbup_area;
    private  String plot_no;
    private  String khata_no;  // need to rename (Khata_no) in DB (in DB Khata_no 'K' is in caps need to change in small 'k').
    private Timestamp stampdate;
    private  Long user_id;
    private  Integer status;
    private  String is_mobile_tower;
    private  String old_property;
    private  String widow_case;
    private  String phys_disable;
    private  String school_case;
    private  String complex_case;
    private  String bpl_category;
    private  String isdp_case;
    private  String builder_case;
    @NonNull
    private  Long entry_fy_id;
    private  Long usage_type_id;
    private  String building_name;
    private  String application_no;
    private  Integer approval_status;
    private  Timestamp entry_date;
    private  String rain_harvest;
    private  Long approval_user;
    private  String consumer_no;
    private  Long old_ward_id;
    private  Long update_ward_user_id;
    private  String penltydisc;
    private BigDecimal plot_area;
    //newly implemented
    @Enumerated(EnumType.STRING)
    private BuildingType buildingtype;
   // private String govTap_Conn;


}