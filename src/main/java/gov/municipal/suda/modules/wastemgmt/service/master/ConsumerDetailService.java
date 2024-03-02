package gov.municipal.suda.modules.wastemgmt.service.master;

import gov.municipal.suda.modules.wastemgmt.dto.ConsumerEntryDTO;

public interface ConsumerDetailService {
    String addConsumerDetails(Long id, ConsumerEntryDTO consumerEntryDTO,String consumer_no);
}
