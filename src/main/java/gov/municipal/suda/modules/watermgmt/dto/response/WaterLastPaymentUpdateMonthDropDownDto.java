package gov.municipal.suda.modules.watermgmt.dto.response;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WaterLastPaymentUpdateMonthDropDownDto {
    private String monthId;
    private String month;
    private String fullDate;
    private String fromMonth;

}
