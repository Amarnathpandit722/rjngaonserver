package gov.municipal.suda.modules.property.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@ToString
public class PaymentReceiptDetailsDTO {
    private String transaction_no;
    private BigDecimal property_tax_arrear;
    private BigDecimal property_tax;
    private BigDecimal smerik_kar_arrear;
    private BigDecimal samerik_kar;
    private BigDecimal education_cess_arrear;
    private BigDecimal education_cess;
    private String previousEffectYear;
    private String currentEffectYear;



}
