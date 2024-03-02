package gov.municipal.suda.modules.property.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
@ToString
public class CollectionReportByPaymentModeResponse {
    private List<CollectionViewByPayModeDTO> data;
    private BigDecimal total_collection;
}
