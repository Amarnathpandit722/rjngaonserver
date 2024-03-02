package gov.municipal.suda.modules.property.service.master;

import gov.municipal.suda.modules.property.model.master.PropertyTypeBean;

import java.util.List;
import java.util.Optional;

public interface PropertyTypeService {

    Optional<List<PropertyTypeBean>> findAll();
}
