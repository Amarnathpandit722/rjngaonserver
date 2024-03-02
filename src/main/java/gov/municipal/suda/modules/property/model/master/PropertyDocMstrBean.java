package gov.municipal.suda.modules.property.model.master;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tbl_prop_document_mstr")
public class PropertyDocMstrBean {
    @Id
    @Generated
    private Long id;
    private String doc_name;
    private Long user_id;
    private LocalDateTime stampdate;
    private Long status;
    private Long is_mandatory;
}
