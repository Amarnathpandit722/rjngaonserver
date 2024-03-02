package gov.municipal.suda.modules.watermgmt.dto.transaction;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WaterPaymentReceiptDto {
    private String receiptNo;
    private String departmentSection;
    private String accountDescription;
    private String description;
    private String wardId;
    private String date;
    private String consumerNo;
    private String propertyNo;
    private String propertyType;
    private String consumerName;
    private String relation;
    private String guardianName;
    private String address;
    private Long mobileNo;
    private BigDecimal payableAmount;
    private String modeOfPayment;
    private String bankName;
    private String branchName;
    private String branchLocation;
    private BigDecimal arrearAmount;
    private String totalPeriod;
    private BigDecimal penaltyAmount;
    private BigDecimal adjustmentAmount;
    private BigDecimal total;
    private BigDecimal receivableAmount;
    private String drawnOn;
    private String usesTypeName;
    private String chequeNo;
    private String chequeDate;
    private String cardHolderName;
    private String cardType;
    private Long chequeStatus;

}
