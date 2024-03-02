package gov.municipal.suda.modules.property.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropDemandDetailsDTO {

    private BigDecimal property_tax_current;
    private BigDecimal property_tax_arrear;
    private BigDecimal composite_tax_current;
    private BigDecimal composite_tax_arrear;
    private BigDecimal common_wtr_tax_current;
    private BigDecimal common_wtr_tax_arrear;
    private BigDecimal penal_charge;
    private BigDecimal penalty;
	
}
