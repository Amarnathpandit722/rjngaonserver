package gov.municipal.suda.modules.property.model.master;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_floor_mstr")
public class FloorBean {
    @Id
    private Long id;
    private String floor_name;
    private String status ;
    private BigDecimal discount_per;
    private String short_name;
}
