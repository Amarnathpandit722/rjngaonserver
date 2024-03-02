package gov.municipal.suda.modules.watermgmt.model.master;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_connection_type_mstr",schema = "watermgmt", catalog = "")
public class ConnectionTypeMasterBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String conn_type;
    private  Integer status;
}
