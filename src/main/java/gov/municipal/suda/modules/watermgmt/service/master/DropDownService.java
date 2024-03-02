package gov.municipal.suda.modules.watermgmt.service.master;

import gov.municipal.suda.modules.property.model.master.SAFAllDropDownBean;
import gov.municipal.suda.modules.watermgmt.dto.transaction.WaterAllDropDownDto;

import java.util.Optional;

public interface DropDownService {

    Optional<WaterAllDropDownDto> findAllDropDown();

}
