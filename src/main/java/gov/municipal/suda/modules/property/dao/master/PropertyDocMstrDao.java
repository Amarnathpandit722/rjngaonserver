package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.model.master.PropertyDocMstrBean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyDocMstrDao extends JpaRepository<PropertyDocMstrBean, Long> {
}
