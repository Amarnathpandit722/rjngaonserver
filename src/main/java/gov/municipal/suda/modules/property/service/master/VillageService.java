
package gov.municipal.suda.modules.property.service.master;

import gov.municipal.suda.modules.property.model.master.VillageBean;

import java.util.List;

public interface VillageService {

    List<VillageBean> fetchAllVillage();

    Object getAllVWZ();

}