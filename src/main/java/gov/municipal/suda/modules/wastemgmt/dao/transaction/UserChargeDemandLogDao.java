package gov.municipal.suda.modules.wastemgmt.dao.transaction;

import gov.municipal.suda.modules.wastemgmt.model.transaction.UserChargeDemandLogBean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChargeDemandLogDao extends JpaRepository<UserChargeDemandLogBean,Long> {
}
