package gov.municipal.suda.modules.wastemgmt.dao.transaction;

import gov.municipal.suda.modules.wastemgmt.model.transaction.ConsumerRateDetailsBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ConsumerRateDetailsDao extends JpaRepository<ConsumerRateDetailsBean,Long> {
    @Query("select d from ConsumerRateDetailsBean d where d.consumer_details_id=?1")
    List<ConsumerRateDetailsBean> findByCosumDetailId(Long consumer_details_id);
    @Query("select count(*) from ConsumerRateDetailsBean r where r.consumer_details_id=?1 and r.consumer_cat_mstr_id=?2 and r.consumer_range_mstr_id=?3")
    Integer checkDuplicateCategory(Long consumerDetailsId, Long consumerCategoryMasterId, Long consumerRangeMasterId);
    @Query("select r from ConsumerRateDetailsBean r where r.consumer_details_id=?1 and r.consumer_range_mstr_id=?2 ") //and r.status=1
    ConsumerRateDetailsBean findConsumerRangeDtlsByDetailsIdAndRangeMasterId(Long consumerDetailsId, Long rangeId);
}
