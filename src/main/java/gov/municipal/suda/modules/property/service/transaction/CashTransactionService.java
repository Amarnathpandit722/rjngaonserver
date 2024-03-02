package gov.municipal.suda.modules.property.service.transaction;

import gov.municipal.suda.modules.property.dto.*;
import gov.municipal.suda.modules.property.model.transaction.PropertyTransactionBean;
import gov.municipal.suda.modules.property.model.transaction.TransactionDeactiveBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CashTransactionService {
    String makeCashTransaction(CashPaymentRequestDto cashPaymentRequestDto)throws Exception;

    List<PaymentViewBeforeTransDTO> paymentView(PaymentViewBeforeTransDTO paymentViewBeforeTransDTO);

    Page<PaymentTransactionViewDTO> paymentTransactionView(String fromDate, String toDate,String payment_mode, Pageable pageable);

    Optional<ReconcillationViewDTO> reconciliationViewById(Long id);

    void reconciliationUpdateById(ReconcillationUpdateDTO reconcillationUpdateDTO);

    List<TransactionViewUpdateDTO> transactionViewByTrnNo(String transactionNo);

    void transactionModeUpdate(TransactionViewUpdateDTO transactionViewUpdateDTO);

    void transactionDeactivate(MultipartFile approvalLetter, Long propId, Long transactionId, Long userId, Long wardId, String reason,String stampdate) throws IOException;
}
