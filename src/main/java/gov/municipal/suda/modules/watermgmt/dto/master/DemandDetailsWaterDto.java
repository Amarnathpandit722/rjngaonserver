package gov.municipal.suda.modules.watermgmt.dto.master;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DemandDetailsWaterDto {
    private String demandFrom;
    private BigDecimal unitRate;
    private Long connectionType;
    private BigDecimal arrearAmount;

}
