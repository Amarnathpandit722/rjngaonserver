package gov.municipal.suda.modules.property.model.master;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_occupation_type")
public class OccupationTypeBean {

    @Id
    @Generated
    private Long id;
    private String occup_type;
    private String status;

}
