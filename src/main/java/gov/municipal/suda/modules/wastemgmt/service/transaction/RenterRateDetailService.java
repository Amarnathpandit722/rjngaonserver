package gov.municipal.suda.modules.wastemgmt.service.transaction;

import gov.municipal.suda.modules.wastemgmt.dto.RenterEntryDTO;

public interface RenterRateDetailService {
    String addRenterRate(Long id, RenterEntryDTO renterEntryDTO);
}
