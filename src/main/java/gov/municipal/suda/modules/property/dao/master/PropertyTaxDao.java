package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.model.transaction.PropertyTaxBean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyTaxDao extends JpaRepository<PropertyTaxBean, Long> {
}
