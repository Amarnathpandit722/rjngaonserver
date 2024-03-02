package gov.municipal.suda.modules.property.model.master;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_zone_mstr")
public class ZoneBean {
    @Id
    private Long id;
    private String zone_code;
    private String zone_name;
    private String stamp_date;
    private Long user_id;
    private String status;

  
}
