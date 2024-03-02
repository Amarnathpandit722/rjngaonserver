package gov.municipal.suda.modules.property.model.master;

import lombok.*;

import javax.persistence.*;

import gov.municipal.suda.util.enumtype.BuildingAge;
import gov.municipal.suda.util.enumtype.BuildingType;
import gov.municipal.suda.util.enumtype.FloorType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_arvrate_mstr")
public class ArvRateBean {
   @Id
   @Generated
    private Long id;
    private Long zone_id;
    private Long road_id;
    private String const_type;
    private Long usestype_id;
    private BigDecimal land_rate;
    private BigDecimal building_rate;
    private String doe;
    private String stampdate;
    private String status;
    @Enumerated(EnumType.STRING)
    private FloorType floor_type;
    @Enumerated(EnumType.STRING)
    private  BuildingType buildingtype;
    @Enumerated(EnumType.STRING)
    private BuildingAge buildingAge;
    
    

}
