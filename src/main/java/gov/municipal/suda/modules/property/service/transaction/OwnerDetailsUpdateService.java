package gov.municipal.suda.modules.property.service.transaction;

import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.modules.property.dto.SAFEntryRequestDto;

public interface OwnerDetailsUpdateService {
    String updateOwnerDetails(Long prop_id, SAFEntryRequestDto safEntryRequestDto) throws Exception;
}
