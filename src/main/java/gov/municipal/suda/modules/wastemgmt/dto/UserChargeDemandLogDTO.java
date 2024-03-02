package gov.municipal.suda.modules.wastemgmt.dto;

import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserChargeDemandLogDTO {
    private Long consumer_detail_id;
    private BigDecimal total_amt;
    private Long user_id;
    private String fromdate; // format yyyy-mm-dd
    private String todate; // format yyyy-mm-dd
}
