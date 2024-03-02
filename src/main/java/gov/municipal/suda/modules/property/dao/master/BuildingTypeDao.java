package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.model.master.BuildingTypeBean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingTypeDao extends JpaRepository<BuildingTypeBean, Long> {
}
