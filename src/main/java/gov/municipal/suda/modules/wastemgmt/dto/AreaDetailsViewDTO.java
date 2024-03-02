package gov.municipal.suda.modules.wastemgmt.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AreaDetailsViewDTO {
    private Long consumerRateId;
    private String consumer_category;
    private String consumer_range;
    private BigDecimal noof_sqft_truck_room;
    private String doe;
    private BigDecimal amount;
    private Long old_rate_chart_id;
}
