package gov.municipal.suda.modules.property.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Table;
import java.math.BigDecimal;
@Getter
@Setter
@ToString
@AllArgsConstructor

public class ARVRangePercentageDTO {
    private Long id;
    private BigDecimal percentage;
    private BigDecimal commercial_per;
}
