package gov.municipal.suda.modules.property.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
//@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CollectionsBodyDto {
    private String consumerNo;
    private String wardNo;
    private String ownerName;
    private String fromMonth;
    //private Long mobileNo;
    //private String fromYear;
    private String upToMonth;
    private String transactionDate;
    private String transactionNo;
    private String modeOfPayment;
    private String chequeDDNo;
    private String bankName;
    private String tcName;
    private BigDecimal paidAmount;
}
