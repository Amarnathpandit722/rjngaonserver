package gov.municipal.suda.modules.wastemgmt.dao.master;

import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerDetailsBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConsumerDetailsDao extends JpaRepository<ConsumerDetailsBean,Long> {
    @Query("select p from ConsumerDetailsBean p where p.consumer_no=?1")
    Optional<ConsumerDetailsBean> findByMstrIdConsumNo(String consumerNo);

    @Query("select p.consumer_mstr_id from ConsumerDetailsBean p where  p.consumer_no=?1")
    Long findConsumerMasterIdByConsumerNo(String consumerNo);



    @Query("select p.id from ConsumerDetailsBean p where  p.consumer_no=?1")
    Long findConsumerDetailsIdByConsumerNo(String consumerNo);
    @Query("select p.consumer_mstr_id from ConsumerDetailsBean p where p.consumer_no=?1")
    Long findConsumMstrIdByNo(String consumerNo);
    @Query("select p from ConsumerDetailsBean p where p.consumer_no=?1")
    Optional<ConsumerDetailsBean> findConsumDetailsIdByNo(String consumerNo);

    @Query("select p from ConsumerDetailsBean p where p.consumer_mstr_id=?1 and p.consumer_type='Self' ")
    Optional<ConsumerDetailsBean> findConsumerDetailsByConsumerMasterId(Long consumerMasterId);

    @Query("select p from ConsumerDetailsBean p where p.consumer_no=?1 and p.consumer_type='Self' ")
    Optional<ConsumerDetailsBean> findConsumerDetailsByConsumerNo(String consumerNo);


}
