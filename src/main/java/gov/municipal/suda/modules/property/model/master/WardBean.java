package gov.municipal.suda.modules.property.model.master;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_ward_mstr")
public class WardBean {
    @Id
    private Long id;
    private Long zone_mstr_id;
    private String ward_name;
    private String area_name;
    private String stampdate;
    private Long user_id;
    private String status;
}
