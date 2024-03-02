package gov.municipal.suda.modules.wastemgmt.service.master;

import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.modules.wastemgmt.dto.AreaDetailsDto;
import gov.municipal.suda.modules.wastemgmt.dto.ConsumerRateDetailsDto;

import java.util.List;

public interface ConsumerRateDetailService {
    String addConsumerRate(Long consumerDetailsId, List<AreaDetailsDto> areDetails);
    void consumerRateDetailsUpdate(String consumerNo, Long rateId, Long previousChartId, AreaDetailsDto areaDetailsDto);
    void consumerCategoryEntryInRateDetailsByConsumerDetailsId(ConsumerRateDetailsDto consumerRateDetailsDto);

}
