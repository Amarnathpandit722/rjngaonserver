package gov.municipal.suda.modules.property.service.transaction;

import gov.municipal.suda.message.ResponseMessage;
import gov.municipal.suda.modules.property.dto.SAFEntryRequestDto;

public interface SAFUpdateService {
    String SAFUpdate (SAFEntryRequestDto safEntryRequestDto, Long id) throws Exception;
}
