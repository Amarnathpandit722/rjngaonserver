package gov.municipal.suda.modules.property.dao.transaction;

import gov.municipal.suda.modules.property.model.transaction.TransactionDeactiveBean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionDeactivationDao extends JpaRepository<TransactionDeactiveBean,Long> {
}
