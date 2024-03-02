package gov.municipal.suda.modules.wastemgmt.dao.transaction;

import gov.municipal.suda.modules.wastemgmt.model.transaction.ConsumerLastPaymentBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConsumerLastPaymentRecordDao extends JpaRepository<ConsumerLastPaymentBean,Long> {
    @Query("select  max(p.id)+1 from ConsumerLastPaymentBean p")
    Long generateLastPaymentId();
    @Query("select  p from ConsumerLastPaymentBean p where p.consumer_details_id=?1")
    List<ConsumerLastPaymentBean> fetchLastPaymentRecord(Long consumerDetailId);
}
