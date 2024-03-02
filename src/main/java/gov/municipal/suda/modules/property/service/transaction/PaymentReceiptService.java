package gov.municipal.suda.modules.property.service.transaction;

import gov.municipal.suda.modules.property.dto.BulkPaymentReceipt;
import gov.municipal.suda.modules.property.dto.PaymentReceiptResponse;

import java.util.List;

public interface PaymentReceiptService {
    List<PaymentReceiptResponse> getPaymentReceipt(String property_no, String from_date, String to_date,String trn_no,String paymentMode);

    List<BulkPaymentReceipt> getPaymentReceiptByTC(String frmDate, String toDate,String user_id);
}
