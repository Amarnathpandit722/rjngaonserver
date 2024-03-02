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
@Table(name="tbl_range_mstr",schema = "watermgmt", catalog = "")
public class WaterRangeMasterBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long prop_type_id;
    private Long room_table_from;
    private Long room_table_upto;
    private String date_of_effect;
    private Integer status;


}
