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
@Table(name = "tbl_property_entry_type")
public class EntryTypeBean {
    @Id
    @Generated
    private Long id;
    private String entry_type;
    private String status;
}
