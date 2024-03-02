package gov.municipal.suda.modules.wastemgmt.dao.master;

import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerCategoryMasterBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerCategoryDao extends JpaRepository<ConsumerCategoryMasterBean,Long> {
}
