package gov.municipal.suda.util.common;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LastPaymentUpdateMonthDropDownDto {
    private String monthId;
    private String month;
    private String fullDate;
    private String fromDate;
}
