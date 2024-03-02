package gov.municipal.suda.modules.wastemgmt.model.master;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_consumer_category_master",schema = "wastemgmt", catalog = "")
public class ConsumerCategoryMasterBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category_name;
    private String description;
    private Integer status;

    @OneToMany(fetch = FetchType.LAZY,mappedBy="consumerCategoryMasterBean")
    private Set<ConsumerRangeMasterBean> consumerRange=new HashSet<>();
  //  private Long user_id;
    //private Timestamp stampdate;
}
