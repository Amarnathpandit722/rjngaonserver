package gov.municipal.suda.modules.watermgmt.service.transaction;

import gov.municipal.suda.modules.watermgmt.dto.response.WaterLastPaymentUpdateMonthDropDownDto;
import gov.municipal.suda.modules.watermgmt.dto.transaction.WaterLastPaymentUpdateDto;

import gov.municipal.suda.modules.watermgmt.dto.transaction.WaterLastPaymentViewResponseDto;
import gov.municipal.suda.modules.watermgmt.dto.transaction.WaterLastPaymentViewResponseMainDto;
import gov.municipal.suda.util.common.LastPaymentUpdateMonthDropDownDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

public interface WaterLastPaymentUpdateService {
    void WaterLastPaymentUpdate(MultipartFile file, Long consumer_details_id, String receipt_no, String receipt_date, String book_no, String frm_month,
                                String upTo_month, BigDecimal tot_amount, Long user_id, String fromDate, String upToDate) throws IOException, ParseException;
    WaterLastPaymentViewResponseMainDto LastPaymentUpdateView(String consumerNo, String fromDate, String demandUpTo);
    List<WaterLastPaymentUpdateMonthDropDownDto> WaterLastPaymentUpdateDropDown(String consumerNo);
}
