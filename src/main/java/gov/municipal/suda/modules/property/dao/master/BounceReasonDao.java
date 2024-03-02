package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.model.master.ChequeDDBounceReasonBean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BounceReasonDao extends JpaRepository<ChequeDDBounceReasonBean,Long> {
}
