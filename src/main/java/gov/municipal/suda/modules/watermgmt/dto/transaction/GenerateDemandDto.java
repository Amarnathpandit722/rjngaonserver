package gov.municipal.suda.modules.watermgmt.dto.transaction;

import gov.municipal.suda.exception.BadRequestException;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@ToString
@NoArgsConstructor

public class GenerateDemandDto {
    @NonNull
    private String consumerNo;
    @NonNull
    private String wardNo;
    @NonNull
    private String effectFrom;
    private Long userId;

    private GenerateDemandDto(final String consumerNo, final String wardNo, final String effectFrom, final Long userId) {
        if(StringUtils.isBlank(consumerNo)) {
            throw new BadRequestException("Consumer Number Can't be blank/empty/null");
        }
        if(StringUtils.isBlank(wardNo)) {
            throw new BadRequestException("Ward can't be blank/empty/null");
        }
        if(StringUtils.isBlank(effectFrom)) {
            throw new BadRequestException("Demand Effect From Date can't be blank/empty/null");
        }

        this.consumerNo=consumerNo;
        this.wardNo=wardNo;
        this.effectFrom=effectFrom;
        this.userId=userId;
    }
}
