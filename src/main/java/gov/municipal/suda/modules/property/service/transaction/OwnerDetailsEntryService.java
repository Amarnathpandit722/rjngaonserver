package gov.municipal.suda.modules.property.service.transaction;

import gov.municipal.suda.modules.property.dto.SAFEntryRequestDto;
import gov.municipal.suda.modules.property.model.master.OwnerDetailsBean;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OwnerDetailsEntryService {

    String createOwnerDetailsEntry(Long prop_id, SAFEntryRequestDto safEntryRequestDto) throws Exception;

    List<OwnerDetailsBean> getPropertyOwnerDetailsByPropId(Long propId);

    String ownerUpdate(OwnerDetailsBean ownerDetailsBean) throws Exception;

    List<OwnerDetailsBean> getPropertyOwnerDetailsByPropNo(String propertyNo);
}
