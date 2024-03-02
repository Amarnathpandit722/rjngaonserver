package gov.municipal.suda.modules.property.dao.transaction;

import gov.municipal.suda.modules.property.model.transaction.ChequeDDCardTransactionBean;
import gov.municipal.suda.modules.property.model.transaction.DemandDetailsBean;
import gov.municipal.suda.modules.property.model.transaction.PropertyTransactionBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.w3c.dom.stylesheets.LinkStyle;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;

public interface CashTransactionDetailDao extends JpaRepository<ChequeDDCardTransactionBean, Long> {
    @Query("select p from ChequeDDCardTransactionBean p where p.transaction_id=?1")
    Optional<ChequeDDCardTransactionBean> findReconUpdateById(Long transaction_id);

    @Query("select p.check_status from ChequeDDCardTransactionBean p where p.transaction_id=?1")
    String findChkStatusByTrnId(Long transaction_id);
    @Query("select  count(p) from ChequeDDCardTransactionBean p where p.transaction_id=?1")
    Long transactionRecorCount(Long transaction_no);
    @Query("select  p from ChequeDDCardTransactionBean p where p.transaction_id=?1")
    ChequeDDCardTransactionBean findTransactionDetailsByTransactionId(Long transaction_id);
    @Query("select  p from ChequeDDCardTransactionBean p where p.id=?1")
    ChequeDDCardTransactionBean fetchTransactionDetailsById(Long id);

}
