package gov.municipal.suda.modules.watermgmt.dao.transaction;

import gov.municipal.suda.modules.watermgmt.model.master.WaterConsumerDetailsBean;
import gov.municipal.suda.modules.watermgmt.model.transaction.WaterTransactionDetailsBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface WaterTransactionDetailsDao extends JpaRepository<WaterTransactionDetailsBean, Long> {
    @Query("select t from WaterTransactionDetailsBean t where t.consumer_dets_id=?1 and t.chk_dd_clear_status=1")
    Optional<WaterTransactionDetailsBean> getCheckAndDDByClearanceStatus(Long consumerDetailsId);
    @Query("select d from WaterTransactionDetailsBean d where d.transaction_id=?1")
    WaterTransactionDetailsBean findByTransactionId(Long tranId);



}
