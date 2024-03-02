package gov.municipal.suda.modules.watermgmt.dao.transaction;

import gov.municipal.suda.modules.watermgmt.model.transaction.WaterTransactionMasterBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface WaterTransactionMasterDao extends JpaRepository<WaterTransactionMasterBean, Long> {
    @Query("select m from WaterTransactionMasterBean m where m.consumer_dets_id=?1 and m.cancel_status=1")
    List<WaterTransactionMasterBean> getAllTransactionByConsumerId(Long consumer_dets_id);
    @Query("select max(m.id)+1 from WaterTransactionMasterBean m ")
    Long getMaxTransactionId();
    @Query("select t from WaterTransactionMasterBean t where  to_Date(t.transaction_date,'DD/MM/YYYY') >= ?1 and to_Date(t.transaction_date,'DD/MM/YYYY') <= ?2")
    List<WaterTransactionMasterBean> getCollectionReport(Date fromDate, Date toDate);

    @Query("select t from WaterTransactionMasterBean t where  to_Date(t.transaction_date,'DD/MM/YYYY') >= ?1 and to_Date(t.transaction_date,'DD/MM/YYYY') <= ?2 and t.ward_id=?3")
    List<WaterTransactionMasterBean> getCollectionReportByWardId(Date fromDate, Date toDate, Long ward_id);

    @Query("select t from WaterTransactionMasterBean t where  to_Date(t.transaction_date,'DD/MM/YYYY') >= ?1 " +
            "and to_Date(t.transaction_date,'DD/MM/YYYY') <= ?2 and t.ward_id=?3 and t.payment_mode=?4")
    List<WaterTransactionMasterBean> getCollectionReportByWardIdAndPaymentMode(Date fromDate, Date toDate, Long ward_id,String paymentMode);

    @Query("select t from WaterTransactionMasterBean t where  to_Date(t.transaction_date,'DD/MM/YYYY') >= ?1 " +
            "and to_Date(t.transaction_date,'DD/MM/YYYY') <= ?2 and t.ward_id=?3 and t.payment_mode=?4 and t.user_id=?5")
    List<WaterTransactionMasterBean> getCollectionReportByWardIdAndPaymentModeAndUserId(Date fromDate, Date toDate, Long ward_id,String paymentMode,Long userId);
}
