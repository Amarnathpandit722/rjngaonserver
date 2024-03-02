package gov.municipal.suda.modules.watermgmt.model.master;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_prop_type_mstr",schema = "watermgmt", catalog = "")
public class PropertyTypeWaterMasterBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String prop_type;
    private Integer status;
}
