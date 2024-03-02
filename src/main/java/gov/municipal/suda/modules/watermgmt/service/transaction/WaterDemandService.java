package gov.municipal.suda.modules.watermgmt.service.transaction;

import gov.municipal.suda.modules.watermgmt.dto.transaction.WaterDemandPrintLogDto;

public interface WaterDemandService {
    void generateSingleDemand(String consumerNo);
    void demandPrintLogEntry(WaterDemandPrintLogDto dto);
}
