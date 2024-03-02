package gov.municipal.suda.modules.watermgmt.repo;

import gov.municipal.suda.modules.watermgmt.model.master.ConsumerBasicDetailsBean;

import java.util.List;

public interface WaterCustomConsumerBasicSearch {
    List<ConsumerBasicDetailsBean> findConsumerBasicDetailsByMobileAndConsumerName(String consumerName, Long mobileNo);
}
