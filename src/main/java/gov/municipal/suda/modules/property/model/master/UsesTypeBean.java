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
@Table(name = "tbl_uses_type")
public class UsesTypeBean {
    @Id
    @Generated
    private Long id;
    private String uses_type_name;
    private String status;
    private String short_name;

}
