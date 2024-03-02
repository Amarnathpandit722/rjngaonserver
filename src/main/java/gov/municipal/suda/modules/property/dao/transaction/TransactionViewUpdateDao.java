package gov.municipal.suda.modules.property.dao.transaction;

import gov.municipal.suda.modules.property.dto.TransactionViewUpdateDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

public interface TransactionViewUpdateDao extends JpaRepository<TransactionViewUpdateDTO,Long> {
    @Query(value = "select d from TransactionViewUpdateDTO d where d.transaction_no=?1")
    List<TransactionViewUpdateDTO> transactionViewByTrnNo(String transaction_no);
    @Query(value="select d from TransactionViewUpdateDTO d where d.transaction_no=?1 and d.status=1 and LOWER(d.payment_mode)=LOWER('Cash') and d.cash_verify_stts=0")
    List<TransactionViewUpdateDTO> transactionViewByTrnNoAndPaymentModeByCash(String tran_no);
    @Query(value="select d from TransactionViewUpdateDTO d where d.transaction_no=?1 and d.status=1 and LOWER(d.payment_mode)=LOWER('Cheque') and d.cheque_status=0")
    List<TransactionViewUpdateDTO> transactionViewByTrnNoAndPaymentModeByCheque(String tran_no);

    @Query(value = "select d from TransactionViewUpdateDTO d where d.transaction_no=?1 and d.status=1")
    List<TransactionViewUpdateDTO> transactionViewByTrnNoByActiveStatus(String transaction_no);
}
