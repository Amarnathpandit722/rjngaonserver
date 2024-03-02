package gov.municipal.suda.modules.property.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CollectionBouncedChequeDDResponseDto {
    //private Long ward_id;
    //private BigDecimal acomposite_tax;
    //private BigDecimal aproperty_tax;
    //private String bank_name;
   // private String branch_name;
  //  private String cheque_dd_aprcode;
    //private BigDecimal composite_tax;
    //private BigDecimal discount;
    //private BigDecimal education_cess;
    //private BigDecimal aeducation_cess;
    //private BigDecimal form_fee;
   // private Long mobile_no;
    //private String owner_name;

    private String payment_mode;
    private Long no_of_transaction; // this field naming convention came from the frontEnd developer side
    //private BigDecimal penal_charge;
    //private BigDecimal penalty;
    //private String property_no;
   // private BigDecimal property_tax;
    //private BigDecimal rain_harvest_charge;
   // private String stampdate;
    //private String tax_collector;
    private BigDecimal tot_amount;
   // private String transaction_date;
   // private String transaction_no;
   // private Long user_id;
   // private String ward_name;
    //private  String for_year;
}
