package gov.municipal.suda.modules.wastemgmt.repos;

import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerDetailsBean;

import java.util.List;

public interface UserChargeCustomConsumerDetailsSearch {
    List<ConsumerDetailsBean> fetchCustomConsumerDetailsSearch(String consumer_no, String consumer_name,Long mobile_no);
}
