package gov.municipal.suda.modules.property.dto;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@ToString
public class CollectionViewByPayModeDTO {

    private List<ModeWiseCollectionDto> modeWise;
    private BigDecimal property_tax;
    private BigDecimal sanitation_tax;
    private BigDecimal composite_tax;
    private BigDecimal common_water_tax;
    private BigDecimal personal_water_tax;
    private BigDecimal education_cess;
    private BigDecimal diff_amount;
    private BigDecimal penal_charge;
    private BigDecimal rain_wtr_harvest;
    private BigDecimal form_fee;
    private BigDecimal penalty;
    private BigDecimal discount;
    private BigDecimal adjustment;
    private BigDecimal account_total;
    private BigDecimal payment_mode_total_collection;
    private BigDecimal payment_mode_net_collection;
}
