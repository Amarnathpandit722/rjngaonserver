package gov.municipal.suda.modules.property.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollectionByPayModeDTO {
    private BigDecimal property_tax;
    private BigDecimal composite_tax;
    private BigDecimal sanitation_tax;
    private BigDecimal common_wtr_tax;
    private BigDecimal penal_charge;
    private BigDecimal personal_wtr_tax;
    private BigDecimal education_cess;
    private BigDecimal diff_amount;
    private BigDecimal rain_harvest_charge;
    private BigInteger form_fee;
    private BigDecimal penalty;
    private BigInteger discount;
    private BigInteger adv_adjust;
}