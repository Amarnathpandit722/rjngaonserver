package gov.municipal.suda.modules.property.service.transaction;

import gov.municipal.suda.message.ResponseMessage;
import gov.municipal.suda.modules.property.dto.SAFEntryRequestDto;
import gov.municipal.suda.modules.property.model.master.PropertyDocDtlBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SAFEntryService {
 String create (SAFEntryRequestDto safEntryRequestDto) throws Exception;
 PropertyDocDtlBean SAFDocumentUpload(MultipartFile file, Long prop_id, Long doc_mstr_id, Long user_id, Long fy_id) throws Exception;
}
