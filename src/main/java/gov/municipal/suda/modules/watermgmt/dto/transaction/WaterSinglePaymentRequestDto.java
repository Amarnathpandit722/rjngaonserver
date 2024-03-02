package gov.municipal.suda.modules.watermgmt.dto.transaction;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WaterSinglePaymentRequestDto {
    private String consumerNo;
    private String paymentMode; // get the name from drop down of mode of payment
    private Long paymentModeId; // get this id from Drop down of mode of payment
    private String wardNo;
    private Long propertyTypeId;
    private String connectionType;
    private String dateOfEffect;
    private Long userId;
    private String ipAddress;
    private String narration;
    private String bankName;
    private String othersBankName;
    private String branch;
    private BigDecimal chequeDDAmount;
    private String chequeDDNo;
    private String chequeDDDate;
    private String cardType;
    private String apprCode;
    private Long cardNo;
    private String cardHolderName;
    private String neftNo;
    private String neftDate;
    private BigDecimal payableAmt;
    private BigDecimal penalty;
    private BigDecimal demandPayment;
    private String rtgsNo;
    private String rtgsDate;
    private String oldWardNo;
    private List<Long> demandId;
}
