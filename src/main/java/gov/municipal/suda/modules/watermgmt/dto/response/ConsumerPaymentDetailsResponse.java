package gov.municipal.suda.modules.watermgmt.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerPaymentDetailsResponse {
    private String transactionDate;
    private String receiptNo;
    private String periods;
    private Long unit;
    private String paymentMode;
    private BigDecimal demand;
    private BigDecimal penalty;
    private BigDecimal payableAmount;
    private Long userId;
    private String bankName;
    private String chequeDate;
    private String branchName;


}
