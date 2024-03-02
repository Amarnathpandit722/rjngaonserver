package gov.municipal.suda.modules.wastemgmt.dao.transaction;

import gov.municipal.suda.modules.wastemgmt.model.transaction.CollectionMasterBean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumerCollectionDao extends JpaRepository<CollectionMasterBean,Long> {
}
