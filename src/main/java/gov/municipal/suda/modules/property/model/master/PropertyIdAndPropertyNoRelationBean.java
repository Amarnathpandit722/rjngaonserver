package gov.municipal.suda.modules.property.model.master;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tbl_prop_usr_rel_rec")
public class PropertyIdAndPropertyNoRelationBean {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private Long ward_id;
    private Long prop_id;
    private String holding_no;  // this is property no
    private String module;
    private String remarks;
    private Long user_id;
    private Timestamp stampdate;
    private Integer status;


}
