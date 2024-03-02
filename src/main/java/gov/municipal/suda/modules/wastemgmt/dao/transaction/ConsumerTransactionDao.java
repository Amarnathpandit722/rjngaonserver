package gov.municipal.suda.modules.wastemgmt.dao.transaction;

import gov.municipal.suda.modules.wastemgmt.model.transaction.ConsumerTransactionBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConsumerTransactionDao extends JpaRepository<ConsumerTransactionBean,Long> {
    @Query("select  p from ConsumerTransactionBean p where p.transaction_no=?1 and p.status=1")
    List<ConsumerTransactionBean> fetchConsumerTrnByNo(String tran_no);
    @Query("select  p from ConsumerTransactionBean p where consumer_detail_id=?1 and p.status=1")
    List<ConsumerTransactionBean> fetchTrnByDtlId(Long consumerDetailsId);
    @Query("select  max(p.id)+1 from ConsumerTransactionBean p")
    Long generateId();
}
