package gov.municipal.suda.modules.watermgmt.dao.transaction;

import gov.municipal.suda.modules.watermgmt.model.transaction.WaterDemandLogPrintBean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaterDemandPrintLogDao extends JpaRepository<WaterDemandLogPrintBean, Long> {
}
