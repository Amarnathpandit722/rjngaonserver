package gov.municipal.suda.modules.property.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReconcillationUpdateDTO {
    private Long transaction_id;
    private Long id;
    private Long user_id;
    private Integer check_status;
    private String remarks;
}
