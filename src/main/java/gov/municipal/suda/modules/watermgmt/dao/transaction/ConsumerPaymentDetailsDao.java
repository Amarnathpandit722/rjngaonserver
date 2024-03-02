package gov.municipal.suda.modules.watermgmt.dao.transaction;
import gov.municipal.suda.modules.watermgmt.model.transaction.ConsumerPaymentDetailsBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConsumerPaymentDetailsDao extends JpaRepository<ConsumerPaymentDetailsBean, Long> {
    @Query("select p from ConsumerPaymentDetailsBean p where p.demand_id=?1 and p.consumer_dets_id=?2")
    Optional<ConsumerPaymentDetailsBean> getConsumerPaymentDetailsByDemandAndConsumerDetailsIdLong(Long demandId, Long consumerDetailsId);
    @Query("select p from ConsumerPaymentDetailsBean p where p.transaction_id=?1 order by TO_DATE(p.from_date,'DD/MM/YYYY') asc")
    //here in database table of from_date fields there are two type of date format value one is YYYY-MM-DD (this format present in the old records) and second is DD/MM/YYYY format, so we consider DD/MM/YYYY format.
    List<ConsumerPaymentDetailsBean> getConsumerPaymentDetailsByTransactionId(Long transactionId);
}
