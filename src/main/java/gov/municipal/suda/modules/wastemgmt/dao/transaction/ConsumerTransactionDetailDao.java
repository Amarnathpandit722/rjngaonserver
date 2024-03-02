package gov.municipal.suda.modules.wastemgmt.dao.transaction;

import gov.municipal.suda.modules.wastemgmt.model.transaction.ConsumerTransactionBean;
import gov.municipal.suda.modules.wastemgmt.model.transaction.ConsumerTransactionDetailBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConsumerTransactionDetailDao extends JpaRepository<ConsumerTransactionDetailBean,Long> {
    @Query("select  p from ConsumerTransactionDetailBean p where transcation_mstr_id=?1")
    Optional<ConsumerTransactionDetailBean> fetchConsumerTrnDetailByMstrId(Long transaction_mstr_id);
}
