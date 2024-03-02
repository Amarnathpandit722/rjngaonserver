package gov.municipal.suda.modules.watermgmt.dto.transaction;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DemandReceiptDto {
    private String department;
    private String printDate;
    private String account;
    private String wardNo;
    private String connectionType;
    private String propertyNo;
    private String propertyType;
    private String consumerNo;
    private String consumerName;
    private Long mobileNo;
    private String address;
    private String meterNo;
    private BigDecimal penalty;
    private BigDecimal totalPayableAmt;
}
