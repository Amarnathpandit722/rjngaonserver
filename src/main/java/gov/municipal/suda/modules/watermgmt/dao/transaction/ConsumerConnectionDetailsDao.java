package gov.municipal.suda.modules.watermgmt.dao.transaction;
import gov.municipal.suda.modules.watermgmt.model.transaction.ConsumerConnectionDetailsBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.OptionalInt;

public interface ConsumerConnectionDetailsDao extends JpaRepository<ConsumerConnectionDetailsBean, Long> {
   @Query("select c from ConsumerConnectionDetailsBean c where c.consumer_dets_id=?1")
    Optional<ConsumerConnectionDetailsBean> fetchConsumerConnectionByConsumerDetailsId(Long consumerDetailId);
}
