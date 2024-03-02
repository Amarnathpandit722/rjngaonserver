package gov.municipal.suda.modules.watermgmt.dto.transaction;

import gov.municipal.suda.modules.property.dto.CollectionsBodyDto;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
//@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CollectionReportDto {
    private List<CollectionsBodyDto> collectionsBody;
    private BigDecimal totalCollection;
    private BigDecimal totalBounce;
    private BigDecimal netCollection;

}
