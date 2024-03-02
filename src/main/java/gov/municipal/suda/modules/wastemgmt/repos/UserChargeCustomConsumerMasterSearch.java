package gov.municipal.suda.modules.wastemgmt.repos;

import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerMasterBean;

import java.util.List;

public interface UserChargeCustomConsumerMasterSearch {
    List<ConsumerMasterBean> fetchCustomConsumerMasterSearch(String ward_id, String holding_no);
}
