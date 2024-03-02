package gov.municipal.suda.modules.wastemgmt.model.transaction;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_consumer_advance",schema = "wastemgmt", catalog = "")
public class ConsumerAdvanceBean{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long ward_id;
    private Long consumer_details_id;
    private Long trans_id;
    private BigDecimal amount;
    private String advance_type;
    private String file_name;
    private String remarks;
    private String reason;
    private Integer status;
    private Long user_id;
    private Timestamp stampdate;
    private Long old_ward_id;
}
