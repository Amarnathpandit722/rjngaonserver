package gov.municipal.suda.modules.property.service.transaction;

import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.modules.property.dto.OwnerTaxDTO;
import gov.municipal.suda.modules.property.dto.SAFEntryRequestDto;
import gov.municipal.suda.modules.property.model.transaction.SAFARVDetailsBean;

import java.util.List;

public interface SAFARVUpdateService {
    String updateSAFARV(Long prop_id, SAFEntryRequestDto safEntryRequestDto) throws BadRequestException;
}
