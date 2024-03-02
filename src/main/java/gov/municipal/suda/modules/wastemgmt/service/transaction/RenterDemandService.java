package gov.municipal.suda.modules.wastemgmt.service.transaction;

import gov.municipal.suda.modules.wastemgmt.dto.AreaDetailsDto;
import gov.municipal.suda.modules.wastemgmt.dto.RenterEntryDTO;

import java.util.List;

public interface RenterDemandService {
    String createRenterDemand(Long id, RenterEntryDTO dto);
}
