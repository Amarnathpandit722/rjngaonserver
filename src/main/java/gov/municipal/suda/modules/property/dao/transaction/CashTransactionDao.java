package gov.municipal.suda.modules.property.dao.transaction;

import gov.municipal.suda.modules.property.model.transaction.PropertyTransactionBean;
import org.apache.tomcat.jni.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

public interface CashTransactionDao extends JpaRepository<PropertyTransactionBean, Long> {

    @Query("select  max(p.id)+1 from PropertyTransactionBean p")
    Long generateId();
    @Query("select  p from PropertyTransactionBean p where prop_id=?1 and p.status=1")
    List<PropertyTransactionBean> fetchTransactionById(Long propId);
    @Query("select  p from PropertyTransactionBean p where p.transaction_no=?1 and p.status=1")
    List<PropertyTransactionBean> fetchTransactionBYTrnNo(String tran_no);
    @Query("select  p from PropertyTransactionBean p where p.user_id=?1 and p.status=1 and p.stampdate between ?2 and ?3")
    List<PropertyTransactionBean> fetchTransactionByUserIdDate(Long user_id, Timestamp frm_date, Timestamp to_date);
    @Query("select  p from PropertyTransactionBean p where transaction_no=?1 and p.status=1 and LOWER(p.payment_mode)=LOWER('Cash') and p.cash_verify_stts=0")
    PropertyTransactionBean fetchTransactionMasterByTransactionNoForCash(String transaction_no);

//    @Query("SELECT p FROM PropertyTransactionBean p where p.prop_id=?1 ORDER BY p.stampdate DESC LIMIT 1")
//    List<PropertyTransactionBean> lastPaymentRecordBeanList(Long prop_id);
    @Modifying
    @Transactional
    @Query("UPDATE PropertyTransactionBean e SET e.status = 5 WHERE e.id = ?1")
    int updateTransactionById(Long id);

}
