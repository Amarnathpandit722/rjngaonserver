package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.model.master.EntryTypeBean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryTypeDao extends JpaRepository<EntryTypeBean, Long> {
}
