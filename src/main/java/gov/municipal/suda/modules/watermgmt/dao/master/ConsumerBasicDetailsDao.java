package gov.municipal.suda.modules.watermgmt.dao.master;

import gov.municipal.suda.modules.watermgmt.model.master.ConsumerBasicDetailsBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ConsumerBasicDetailsDao extends JpaRepository<ConsumerBasicDetailsBean, Long> {
    @Query("select c from ConsumerBasicDetailsBean c where c.consumer_dets_id=?1")
    Optional<ConsumerBasicDetailsBean> getConsumerBasicDetailsByConsumerDetailsId(Long consumerDetailsId);
    @Query("select c from ConsumerBasicDetailsBean c where c.consumer_dets_id IN (:consumerDetailsId)")
    List<ConsumerBasicDetailsBean> getConsumerBasicDetailsByConsumerDetailsIdSets(@Param("consumerDetailsId") Set<Long> consumerDetailsId);
    @Query("select c from ConsumerBasicDetailsBean c where c.mobile_no=?1 OR c.name like '%?2%'")
    List<ConsumerBasicDetailsBean> getConsumerBasicDetailsByMobileAndConsumerName(Long mobileNo, String consumerName);
}
