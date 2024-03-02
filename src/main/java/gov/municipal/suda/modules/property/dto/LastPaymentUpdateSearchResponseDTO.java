package gov.municipal.suda.modules.property.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LastPaymentUpdateSearchResponseDTO {

    private String wardNo;
    private String propertyNo;
    private String propertyAddress;
    private String guardianName;
    private String mobileNo;
}
