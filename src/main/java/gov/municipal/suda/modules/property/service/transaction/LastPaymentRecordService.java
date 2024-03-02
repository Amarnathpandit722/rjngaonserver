package gov.municipal.suda.modules.property.service.transaction;

import gov.municipal.suda.modules.property.dto.LastPaymentResponseDTO;
import gov.municipal.suda.modules.property.dto.LastPaymentUpdateSearchResponseDTO;
import gov.municipal.suda.modules.property.dto.PropertyLastPaymentDetailsViewDTO;
import gov.municipal.suda.modules.property.dto.PropertyLastPaymentUpdateRequestDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

public interface LastPaymentRecordService {
    Long create(Long prop_id,String from_year, String to_year, Long user_id)throws Exception;
    List<LastPaymentUpdateSearchResponseDTO> lastPaymentUpdateSearch(Long wardId, String propertyNo, String ownerName, Long mobileNo);
    LastPaymentResponseDTO lastUpdatePaymentView(String propertyNo);
    PropertyLastPaymentDetailsViewDTO lastPaymentDetailsView(String propertyNo,String upToYear);

    void PropertyLastPaymentUpdate(MultipartFile file,String propertyNo,
                                   String receiptNo, String receiptDate, String bookNo, Long userId,
                                   String fromYear, String upToYear, BigDecimal totalAmount, BigDecimal fineAmount) throws IOException, ParseException;

    /*
      propertyNo,
      receiptNo,
      receiptDate,
      bookNo,
      filename,
      userId,
      fromYear,
      upToYear,
      totalAmount,
      fineAmount
    * */

}
