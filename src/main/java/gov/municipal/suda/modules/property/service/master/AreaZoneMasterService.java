package gov.municipal.suda.modules.property.service.master;

import gov.municipal.suda.modules.property.model.master.AreaZoneMasterBean;

import java.util.List;
import java.util.Optional;

public interface AreaZoneMasterService {
    Optional<List<AreaZoneMasterBean>> findAllAreaZone();
}
