package gov.municipal.suda.modules.property.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PayReceiptFloorDTO {
    private String transaction_no;
    private String usage;
    private String floor_name;
    private String built_up_area;

}
