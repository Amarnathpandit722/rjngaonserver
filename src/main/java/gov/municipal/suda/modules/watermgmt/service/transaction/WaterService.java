package gov.municipal.suda.modules.watermgmt.service.transaction;

import gov.municipal.suda.modules.watermgmt.dto.transaction.GenerateDemandDto;
import gov.municipal.suda.modules.watermgmt.dto.response.ConsumerEntrySuccessResponse;
import gov.municipal.suda.modules.watermgmt.dto.response.ConsumerListResponse;
import gov.municipal.suda.modules.watermgmt.dto.response.ConsumerPaymentDetailsResponse;
import gov.municipal.suda.modules.watermgmt.dto.transaction.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface WaterService {
    ConsumerEntrySuccessResponse addEntry(ConsumerEntryDto consumerEntry);
    WaterPaymentReceiptDto getReceiptDuringNewConsumerEntry(String consumerNo, String modeOfPayment);
    List<ConsumerListResponse> getConsumerList(Long wardId, String propertyNo, String consumerNo, Long mobileNo, String consumerName);
    ConsumerUpdateDto view (String consumerNo);
   String viewDemandAmount(String fromDate, String toDate, BigDecimal arrearAmount
            , String propertyType, Long noOfRoom, String connectionType);
    void singleDemandGenerate(GenerateDemandDto dto);
    void waterPayment(WaterSinglePaymentRequestDto dto);
    List<ConsumerPaymentDetailsResponse> getConsumerPaymentDetails(String consumerNo);
  CollectionReportDto collectionReport(String dateFrom, String dateUpto, String wardId, String userId, String paymentMode);
  ////void IsNagarNigamEmployeeDocumentUpload(MultipartFile file,String consumerNo) throws IOException;
  
  
  
}
