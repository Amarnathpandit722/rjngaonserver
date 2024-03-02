package gov.municipal.suda.modules.watermgmt.dao.transaction;

import gov.municipal.suda.modules.watermgmt.model.transaction.WaterPaymentReceiptBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WaterPaymentReceiptDao extends JpaRepository<WaterPaymentReceiptBean, Long> {
    @Query("select r from WaterPaymentReceiptBean r where r.consumer_dets_id=?1 and r.transaction_id=?2")
    Optional<WaterPaymentReceiptBean> getPaymentReceiptByConsumerIdAndTransactionId(Long consumerId, Long tranId);
}
