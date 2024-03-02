package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.model.master.LegacyEntryTypeBean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LegacyEntryTypeDao extends JpaRepository<LegacyEntryTypeBean, Long> {

}
