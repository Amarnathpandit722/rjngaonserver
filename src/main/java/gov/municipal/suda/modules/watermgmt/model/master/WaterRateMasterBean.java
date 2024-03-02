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
@Table(name="tbl_rate_mstr",schema = "watermgmt", catalog = "")
public class WaterRateMasterBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long prop_type_id;
    private Long conn_type_id;
    private Long range_mstr_id;
    private BigDecimal amount;
    private String date_of_effect;
    private Integer status;
    private BigDecimal amountNonTaxPayer;
    
    
    
}
