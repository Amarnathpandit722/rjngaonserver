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
@Table(name="tbl_road_type")
public class RoadTypeBean {
   @Id
   @Generated
   private Long id;
   private String  road_name;
   private String status ;
}
