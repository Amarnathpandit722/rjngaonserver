package gov.municipal.suda.modules.property.controller.transaction;

import gov.municipal.suda.modules.property.dto.*;
import gov.municipal.suda.modules.property.model.master.OwnerTaxMasterBean;
import gov.municipal.suda.modules.property.model.transaction.PropertyTransactionBean;
import gov.municipal.suda.modules.property.model.transaction.TransactionDeactiveBean;
import gov.municipal.suda.modules.property.service.transaction.CashTransactionService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin
@Slf4j
public class CashTransactionController {
    @Autowired
    CashTransactionService cashTransactionService;
    @PostMapping({"/user/cashTransaction"})
    public ResponseEntity<String> cashEntry(@RequestBody CashPaymentRequestDto cashPaymentRequestDto) throws Exception {
        String returnResponse=cashTransactionService.makeCashTransaction(cashPaymentRequestDto);
        return ResponseEntity.ok(returnResponse);
    }
    // This Method Call When we select demand for payment in SAf Search Form
    @PostMapping({"/user/paymentViewBeforeTransaction"})
    public ResponseEntity<List<PaymentViewBeforeTransDTO>> paymentView(@RequestBody PaymentViewBeforeTransDTO paymentViewBeforeTransDTO){
        return ResponseEntity.ok(cashTransactionService.paymentView(paymentViewBeforeTransDTO));
    }

    @GetMapping({"/user/reconciliationView"})
    public ResponseEntity<Page<PaymentTransactionViewDTO>> paymentTransactionView(@RequestParam("from_date") String from_date,
                                                                                  @RequestParam("to_date") String to_date,
                                                                                  @RequestParam("payment_mode") String payment_mode,
                                                                                  @RequestParam(defaultValue = "0") int page,
                                                                                  @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page,size, Sort.by("id").descending());
        return ResponseEntity.ok(cashTransactionService.paymentTransactionView(from_date,to_date,payment_mode,pageable));
    }
    @GetMapping({"/user/reconciliationViewById"})
    public ResponseEntity<Optional<ReconcillationViewDTO>> reconciliationViewById(@RequestParam("id") Long id ) {
        return ResponseEntity.ok(cashTransactionService.reconciliationViewById(id));
    }
    //Only SuperAdmin will be access to change the Transacition DEtails (It have to implement )
    
    @PutMapping({"/user/reconciliationUpdateById"})
    public void reconciliationUpdateById(@RequestBody ReconcillationUpdateDTO reconcillationUpdateDTO){
        cashTransactionService.reconciliationUpdateById(reconcillationUpdateDTO);
    }
    @GetMapping({"/user/transactionViewByTrnNo"})
    public ResponseEntity<List<TransactionViewUpdateDTO>> transactionViewByTrnNo(@RequestParam("transaction_no") String transaction_no ) {
        return ResponseEntity.ok(cashTransactionService.transactionViewByTrnNo(transaction_no));
    }
    @PutMapping({"/user/transactionModeUpdate"})
    public void transactionModeUpdate(@RequestBody TransactionViewUpdateDTO transactionViewUpdateDTO){
        cashTransactionService.transactionModeUpdate(transactionViewUpdateDTO);
    }
    @PutMapping({"/user/transactionDeactivate"})
    public void transactionDeactivate(@RequestParam("approval_letter") MultipartFile approval_letter,
                                      @RequestParam("prop_id") Long prop_id,
                                      @RequestParam("transaction_id") Long transaction_id,
                                      @RequestParam("user_id") Long user_id,
                                      @RequestParam("ward_id") Long ward_id,
                                      @RequestParam("reason") String reason,
                                      @RequestParam("stampdate") String stampdate) throws IOException {
       /* File file = new File(approval_letter);
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        MultipartFile convertedFile = new MockMultipartFile("file", file.getName(), fileNameMap.getContentTypeFor(file.getName()),new FileInputStream(file));
*/
        cashTransactionService.transactionDeactivate(approval_letter,prop_id,transaction_id,user_id,ward_id,reason,stampdate);
    }
}
