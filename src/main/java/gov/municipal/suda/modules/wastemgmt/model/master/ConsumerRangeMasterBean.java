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
@Table(name="tbl_consumer_range_master",schema = "wastemgmt", catalog = "",uniqueConstraints = @UniqueConstraint(columnNames = {"consumer_cat_mstr_id"}))
public class ConsumerRangeMasterBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long consumer_cat_mstr_id;
    private String range_name;
   // private String description;
    private Integer status;

    @ManyToOne
    @JoinColumn(name="consumer_cat_mstr_id", referencedColumnName="id",
            insertable =  false, updatable = false)
    private ConsumerCategoryMasterBean consumerCategoryMasterBean;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "range")
    private Set<ConsumerRateChartBean> consumerRateChartBean=new HashSet<>();
   // private Long user_id;
    //private Timestamp stampdate;

}
