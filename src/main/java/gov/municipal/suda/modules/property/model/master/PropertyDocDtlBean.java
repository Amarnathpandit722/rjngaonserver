package gov.municipal.suda.modules.property.model.master;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_prop_document_dtls")
public class PropertyDocDtlBean implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long prop_id;
    private Long doc_mstr_id;
    private String uploaded_doc;
    private String remarks;
    private Long user_id;
    private LocalDateTime stampdate;
    private Long fy_id;
    private Long status;
    private Long approval_status;

}
