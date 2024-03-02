package gov.municipal.suda.modules.watermgmt.dao.master;

import gov.municipal.suda.modules.watermgmt.model.master.WaterConsumerDetailsBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WaterConsumerDetailsDao extends JpaRepository<WaterConsumerDetailsBean, Long> {
    @Query("select c from WaterConsumerDetailsBean c where consumer_no=?1 and c.status=1")
    WaterConsumerDetailsBean findIdByConsumerNo(String consumerNo);
    @Query("select c from WaterConsumerDetailsBean c where c.ward_id=?1 and c.holding_no=?2 OR c.consumer_no=?3")
    List<WaterConsumerDetailsBean> listOfConsumerDetails(Long wardId, String propertyId, String consumerNo);

    @Query("select c from WaterConsumerDetailsBean c where c.ward_id=?1")
    List<WaterConsumerDetailsBean> listOfConsumerDetailsByWardId(Long wardId);

    @Query("select c from WaterConsumerDetailsBean c where c.id=?1 and c.ward_id=?1")
    WaterConsumerDetailsBean findByIdAndWardID(Long id, Long wardId);

    @Query("select w from WaterConsumerDetailsBean w where w.ward_id=?1 and w.consumer_no=?2 and w.status=1")
    List<WaterConsumerDetailsBean> listOfConsumerDetailsByWardIdAndConsumerNo(Long wardId, String consumerNo);

    @Query("select w from WaterConsumerDetailsBean w where w.holding_no=?2 and w.status=1")
    List<WaterConsumerDetailsBean> listOfConsumerDetailsByWardIdAndPropertyNo(Long wardId, String propertyNo);

    @Query("select w from WaterConsumerDetailsBean w where (w.ward_id=?1 or ?1 is null) and (w.consumer_no=?2 or ?2 is null) and (w.holding_no=?3 or ?3 is null) and w.status=1")
    List<WaterConsumerDetailsBean> listOfOptionalConsumerDetails(Long wardId, String consumerNo, String propertyNo);

    @Query("select c from WaterConsumerDetailsBean c where c.consumer_no=?1 and c.ward_id=?2 and c.status=1")
    WaterConsumerDetailsBean findIdByConsumerNoAndWardId(String consumerNo,Long wardId);

}
