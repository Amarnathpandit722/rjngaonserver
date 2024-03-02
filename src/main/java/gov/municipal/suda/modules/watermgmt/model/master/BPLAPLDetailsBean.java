package gov.municipal.suda.modules.watermgmt.model.master;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_bpl_apl_details",schema = "watermgmt", catalog = "")
public class BPLAPLDetailsBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category;
    private BigDecimal security_deposite;
    private Integer status;
}
