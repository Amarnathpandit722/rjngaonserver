package gov.municipal.suda.modules.watermgmt.dto.transaction;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
@Getter
@Setter
@ToString
@NoArgsConstructor
public class WaterDemandPrintLogDto {
    @NonNull
    private Long ward_id;
    @NonNull
    private Long consumer_dets_id;
    @NonNull
    private String demand_from;
    @NonNull
    private String demand_upto;
    @NonNull
    private BigDecimal tot_amount;
    @NonNull
    private Long user_id;

    private Long old_ward_id;

    private WaterDemandPrintLogDto(final Long ward_id,final Long consumer_dets_id,
                                   final String demand_from, final String demand_upto,
                                   final BigDecimal tot_amount, final Long user_id, final Long old_ward_id){
        if(ward_id ==null) {
            throw new IllegalArgumentException("Ward Id shouldn't be blank");
        }
        else if (consumer_dets_id ==null) {
            throw new IllegalArgumentException("Consumer No shouldn't be blank");
        } else if (StringUtils.isBlank(demand_from)) {
            throw new IllegalArgumentException("Demand from shouldn't be blank");
        } else if (StringUtils.isBlank(demand_upto)) {
            throw new IllegalArgumentException("Demand up to shouldn't be blank");
        } else if (tot_amount.compareTo(BigDecimal.ZERO)==0 || tot_amount==null) {
            throw new IllegalArgumentException("Total Amount shouldn't be zero or null");
        } else if (user_id == null) {
            throw  new IllegalArgumentException("User Id shouldn't be blank");
        }

        this.ward_id=ward_id;
        this.consumer_dets_id=consumer_dets_id;
        this.demand_from=demand_from;
        this.demand_upto=demand_upto;
        this.tot_amount=tot_amount;
        this.user_id=user_id;
        this.old_ward_id=old_ward_id;

    }
}
