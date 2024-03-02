package gov.municipal.suda.modules.property.service.transaction;

import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.modules.property.dto.FloorWiseOwnerTaxDTO;
import gov.municipal.suda.modules.property.dto.OwnerTaxDTO;
import gov.municipal.suda.modules.property.dto.SAFARVDetailsDTO;
import gov.municipal.suda.modules.property.dto.SAFEntryRequestDto;
import gov.municipal.suda.modules.property.model.master.OwnerTaxMasterBean;
import gov.municipal.suda.modules.property.model.transaction.SAFARVDetailsBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface OwnerTaxMasterService {
    String create(List<FloorWiseOwnerTaxDTO> floorWiseOwnerTax, Map< String, Integer>calculatedAllARV, Map<String, String> uses_type_map, String occup_type) throws BadRequestException;
    List<OwnerTaxMasterBean> getPropertyOwnerTaxByPropId(Long propId);

    String updateOwnerTax(List<FloorWiseOwnerTaxDTO> floorWiseOwnerTax ,Map< String, Integer>calculatedAllARV, Map<String, String> uses_type, String occup_type)throws BadRequestException;
}