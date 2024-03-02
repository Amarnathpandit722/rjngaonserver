package gov.municipal.suda.modules.property.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OwnerTaxDTO {
    private BigDecimal calc_arv;
    private String year;
}
