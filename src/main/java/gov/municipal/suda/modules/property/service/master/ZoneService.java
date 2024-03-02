package gov.municipal.suda.modules.property.service.master;

import gov.municipal.suda.modules.property.model.master.ZoneBean;

import java.util.List;

public interface ZoneService {
    List<ZoneBean> fetchAllZone();
}
