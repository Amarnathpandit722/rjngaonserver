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
@Table(name="tbl_property_type")
public class PropertyTypeBean {

    @Id
    @Generated
    private Long id;
    private String property_type_name;
    private String status;

}
