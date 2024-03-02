package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.model.master.OccupationTypeBean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OccupationTypeDao extends JpaRepository<OccupationTypeBean, Long> {
}
