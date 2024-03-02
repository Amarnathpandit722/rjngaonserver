package gov.municipal.suda.modules.wastemgmt.service.transaction;

import gov.municipal.suda.util.common.LastPaymentUpdateMonthDropDownDto;
import gov.municipal.suda.modules.wastemgmt.dto.UsesChargeLastPaymentUpdateDto;
import gov.municipal.suda.modules.wastemgmt.dto.WasteLastPaymentUpdateDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

public interface LastPaymentUpdateService {
    void LastPaymentUpdate(MultipartFile file, Long consumer_details_id,
                           String receipt_no,
                           String receipt_date,
                           String book_no,
                           String frm_month,
                           String upto_month,
                           BigDecimal tot_amount,
                           Long user_id,
                           String fromDate,
                           String upToDate) throws IOException, ParseException;
    List<UsesChargeLastPaymentUpdateDto> LastPaymentUpdateView(String consumerNo, String demandUpTo);
    List<LastPaymentUpdateMonthDropDownDto> LastPaymentUpdateMonthDropDown(String consumerNo);

}
