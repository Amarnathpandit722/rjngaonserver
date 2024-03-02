package gov.municipal.suda.modules.property.dto;

import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PropertyLastPaymentUpdateRequestDTO {
    private String propertyNo;
    private String receiptNo;
    private String receiptDate;
    private String bookNo;
    private String filename;
    private Long userId;
    private String fromYear; //need first value from the dropDown
    private String upToYear;
    private BigDecimal totalAmount;
    private BigDecimal fineAmount;
}
