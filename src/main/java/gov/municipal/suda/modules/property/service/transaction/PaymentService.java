package gov.municipal.suda.modules.property.service.transaction;

import gov.municipal.suda.modules.property.dto.PaymentReceiptDTO;

import java.util.List;

public interface PaymentService {
    List<PaymentReceiptDTO> getPaymentDetailsByPropId(Long propId);

    List<Object[]> getPaymentReceiptPropId(Long propId, String frmYear, String uptoYear);

}
