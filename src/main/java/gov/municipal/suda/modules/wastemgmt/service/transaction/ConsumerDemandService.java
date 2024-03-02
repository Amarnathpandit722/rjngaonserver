package gov.municipal.suda.modules.wastemgmt.service.transaction;

import gov.municipal.suda.modules.wastemgmt.dto.AreaDetailsDto;
import gov.municipal.suda.modules.wastemgmt.dto.ConsumerEntryDTO;

import java.util.List;

public interface ConsumerDemandService {
    String createConsumerDemand(Long id, List<AreaDetailsDto> demandDto);
    String singleDemandCreationByConsumerDetailsId(Long id, AreaDetailsDto demandDto);
    String updateConsumerDemand(Long consumerDetailsId, AreaDetailsDto areaDetails);
    String updateLastPaymentUpdate(Long consumerDetailsId, String demandFrom, String demandTo);
}
