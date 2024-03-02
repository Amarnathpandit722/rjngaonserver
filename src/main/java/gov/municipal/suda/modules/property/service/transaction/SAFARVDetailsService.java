package gov.municipal.suda.modules.property.service.transaction;

import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.modules.property.dto.SAFEntryRequestDto;
import org.springframework.http.ResponseEntity;

public interface SAFARVDetailsService {
    String createSAFARV(Long prop_id, SAFEntryRequestDto safEntryRequestDto) throws BadRequestException;

}
