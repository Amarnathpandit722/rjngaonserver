package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.model.master.PropertyDocDtlBean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyDocDtlDao extends JpaRepository<PropertyDocDtlBean,Long> {
}
