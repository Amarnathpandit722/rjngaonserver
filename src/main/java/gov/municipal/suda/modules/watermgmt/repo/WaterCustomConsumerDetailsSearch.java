package gov.municipal.suda.modules.watermgmt.repo;

import gov.municipal.suda.modules.watermgmt.model.master.WaterConsumerDetailsBean;

import java.util.List;

public interface WaterCustomConsumerDetailsSearch {
    List<WaterConsumerDetailsBean> findWaterConsumerDetailsBy(Long wardId, String consumerNo, String propertyNo);
}
