package gov.municipal.suda.modules.property.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ArvRateResponseDTO {

    private Long id;
    private BigDecimal land_rate;
    private BigDecimal building_rate;
}
