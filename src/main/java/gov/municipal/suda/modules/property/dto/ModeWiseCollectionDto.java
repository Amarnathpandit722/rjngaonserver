package gov.municipal.suda.modules.property.dto;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ModeWiseCollectionDto {
    private String payment_mode;
    private BigInteger transaction_count;
    private BigDecimal total_collection;
}
