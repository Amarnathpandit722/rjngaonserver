
package gov.municipal.suda.modules.watermgmt.dto.transaction;
import lombok.*;
import java.math.BigDecimal;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WaterLastPaymentUpdateDto {
    private Long consumer_details_id;
    private String receipt_no;
    private String receipt_date;
    private String book_no;
    private String frm_month;
    private String upto_month;
    private BigDecimal tot_amount;
    private Long user_id;
    private String fromDate;
    private String upToDate;
}
