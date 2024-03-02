package gov.municipal.suda.modules.wastemgmt.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WaterRangeDTO {
    private Long id;
    private Long prop_type_id;
    private String ws_range;
    private Integer status;
    private String date_of_effect;
}
