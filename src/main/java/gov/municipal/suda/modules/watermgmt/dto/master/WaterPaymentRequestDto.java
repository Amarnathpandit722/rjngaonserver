package gov.municipal.suda.modules.watermgmt.dto.master;

import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WaterPaymentRequestDto {

    private String consumerNo;
    private Long consumerDetailsId;
    private String paymentMode; // get the name from drop down of mode of payment
    private Long paymentModeId; // get this id from Drop down of mode of payment
    private Long demandId;
    private Long userId;
    private String ipAddress;
    private String narration;
    private String dueFrom;
    private String dueUpTo;
    private String bankName;
    private String othersBankName;
    private String branch;
    private String chequeNo;
    private String chequeDate;
    private String cardType;
    private Long transactionId;
    private Long apprCode;
    private Long cardNo;
    private String cardHolderName;
    private String neftNo;
    private String neftDate;
    private BigDecimal arrearAmt;
    private BigDecimal payableAmt;
    private BigDecimal penalty;
    private BigDecimal discount;
    private BigDecimal demandPayment;
    private String ddNo;
    private String ddDate;
    private String rtgsNo;
    private String rtgsDate;
}
