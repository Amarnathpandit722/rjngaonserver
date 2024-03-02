package gov.municipal.suda.modules.wastemgmt.service.transaction;

import gov.municipal.suda.modules.wastemgmt.dto.UserChargeDemandLogDTO;

public interface UserChargeDemandLogService {
    void createDemandLog(UserChargeDemandLogDTO dto);
}
