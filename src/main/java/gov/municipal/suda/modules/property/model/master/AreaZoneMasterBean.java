package gov.municipal.suda.modules.property.model.master;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_area_zone_mstr")
public class AreaZoneMasterBean {

    @Id
    @Generated
    private Long id;
    private Long ward_id;
    private String zone_name;
    private String stampdate;
    private Long user_id;
    private String status;

}
