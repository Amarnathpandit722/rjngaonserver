package gov.municipal.suda.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;

import gov.municipal.suda.modules.property.service.transaction.CompositeTaxService;
@Slf4j
public class Calculations {
	
	@Autowired
	CompositeTaxService compositeTaxService;

    public static final BigDecimal finalArvRate(BigDecimal builtUpArea,BigDecimal buildingRate, BigDecimal discountPerc) {  // field name = f_calc_rate count final arvRate after floor discount
            BigDecimal calculateBeforeFinalDiscount=buildingRate.multiply(builtUpArea); 
      
            BigDecimal calculateAfterFinalDiscount = calculateBeforeFinalDiscount.multiply(BigDecimal.valueOf(100.00)).divide(BigDecimal.valueOf(100.00));
            BigDecimal discountPercentage = discountPerc ; // Replace with your discount percentage
            return calculateAfterFinalDiscount = calculateBeforeFinalDiscount.subtract(calculateBeforeFinalDiscount.multiply(discountPercentage).divide(BigDecimal.valueOf(100.00)));
      
    }
    public static final BigDecimal totalMaintenance(BigDecimal totalARV, BigDecimal discountperc) {

        return totalARV.multiply(discountperc);
    }

    public static final BigDecimal finalCalcRate(BigDecimal buildingRate, BigDecimal finalArvRate) {
        return buildingRate.subtract(finalArvRate);
    }

    public static final BigDecimal finalCalcArv(BigDecimal totalBuiltupArea,BigDecimal finalArvRateAfterDiscount) {
        return totalBuiltupArea.multiply(finalArvRateAfterDiscount);
    }
    public static final BigDecimal ARV(BigDecimal buildingRate, BigDecimal totalBuiltupArea) {
        return buildingRate.multiply(totalBuiltupArea);
    }

    public  static final BigDecimal rebateOfPhysicalHandicapped(BigDecimal calcArv, BigDecimal discountPercentageOfPH) {
        return calcArv.multiply(discountPercentageOfPH);
    }

    public static final BigDecimal rebateOnResidentialProp( BigDecimal finalARV,String uses_type_name) {
        if(uses_type_name.equalsIgnoreCase("COMMERCIAL")) {
            return finalARV; // 100% rebate
        }
        return finalARV.multiply(BigDecimal.valueOf(0.50)); // 50% rebate if builtupArea > 500

    }
//   



}