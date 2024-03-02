package gov.municipal.suda.modules.wastemgmt.service.master;

import gov.municipal.suda.modules.wastemgmt.dto.RenterEntryDTO;

import java.sql.SQLException;

public interface RenterMasterService {
    String addRenter(RenterEntryDTO renterEntryDTO) throws SQLException;
}
