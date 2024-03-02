package gov.municipal.suda.modules.property.service.master;

import gov.municipal.suda.modules.property.model.master.RoadTypeBean;

import java.util.List;
import java.util.Optional;

public interface RoadTypeService {

    Optional<List<RoadTypeBean>> findAll();
}
