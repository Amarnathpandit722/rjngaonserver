package gov.municipal.suda.modules.wastemgmt.dto;

import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerRangeMasterBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CategoryRangeRateDto {
    private Long id;
    private String category_name;
    private Set<RangeAndRateDto> consumerRangeType;
}
