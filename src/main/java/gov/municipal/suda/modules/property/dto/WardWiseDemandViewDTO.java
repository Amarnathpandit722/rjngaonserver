package gov.municipal.suda.modules.property.dto;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WardWiseDemandViewDTO {

    private String ward_name;
    private String property_no;
    private String owner_name;
    private String guardian_name;
    private BigInteger mobile_no;
    private String owner_address;
    private BigDecimal penalty;
    private BigDecimal demand_amount;
    private BigDecimal total_amount;
    private String fy_name;

}
