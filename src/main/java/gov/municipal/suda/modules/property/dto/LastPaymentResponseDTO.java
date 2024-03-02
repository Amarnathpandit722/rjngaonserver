package gov.municipal.suda.modules.property.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LastPaymentResponseDTO {

    private Integer wardNo;
    private String propertyNo;
    private String propertyAddress;
    private String mohalla;
    private String entryType;
    private BigDecimal totalArea;
    private Integer mobileNo;
    private String orderDate;
    private String plotNo;
    private String khataNo;
    private String memoNo;
    private OwnerViewDTO ownerDetails;
    private List<OwnerTaxViewDTO> taxDetails;
    private List<String> upToYear;
}
