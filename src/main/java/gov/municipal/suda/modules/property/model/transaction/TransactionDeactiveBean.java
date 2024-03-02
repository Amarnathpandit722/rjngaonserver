package gov.municipal.suda.modules.property.model.transaction;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_prop_transaction_deactive")
public class TransactionDeactiveBean {
    @Id
    @GeneratedValue
    private Long id;
    private Long transaction_id;
    private Long prop_id;
    private Long ward_id;
    private Long user_id;
    private String stampdate;
    private String reason;
    private String document;
    private Long old_ward_id;
}
