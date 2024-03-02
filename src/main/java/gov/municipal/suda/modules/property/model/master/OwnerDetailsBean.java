package gov.municipal.suda.modules.property.model.master;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import org.hibernate.annotations.Cache;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_prop_owner_details")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@ToString
public class OwnerDetailsBean implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    //@Id
    @Column(name = "prop_id")
    private Long prop_id;
    private String title;
    private String owner_name;
    private String guardian_name;
    private String gender;
    private String owner_address;
    private String purchase_date;
    private String relation;
    private Long mobile_no;
    private Long aadhar_no;
    private String pan_no;
    private String email_id;
    private Timestamp stampdate;
    private Long user_id;
    private Integer status;
    private String rain_wtr_doc;
    private Long rain_rmv_user; // ask question
    private Timestamp rain_rmv_date;  // ask question
    private String owner_pic;
    private String type_of_change; // ask question
    private String govTap_Conn;
    
    private String b1;
    private String b2;
    private String diversion;
    private String kharidi_Bikri;
    
    
    
//    @JsonIgnore
//    @OneToOne(fetch = FetchType.EAGER,cascade = {CascadeType.ALL})
//    @JoinColumn(name="prop_id", referencedColumnName = "id")
//    private Set<PropertyMasterBean> propertyMaster;

//    @OneToOne
//    @PrimaryKeyJoinColumn(name = "prop_id")
//   private PropertyMasterBean propertyMaster;

}
