package gov.municipal.suda.modules.wastemgmt.dto;

import gov.municipal.suda.modules.property.model.master.FinYearBean;
import gov.municipal.suda.modules.property.model.master.WardBean;
import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerCategoryMasterBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AllDropDownDto {
    private List<WardBean> wards;
    private List<CategoryRangeRateDto> categories;
    private List<FinYearBean> dateOfEffect;

}
