package gov.municipal.suda.modules.wastemgmt.service.master;

import gov.municipal.suda.modules.wastemgmt.dto.*;
import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerCategoryMasterBean;
import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerRangeMasterBean;
import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerRateChartBean;
import gov.municipal.suda.modules.wastemgmt.model.transaction.ConsumerDemandBean;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ConsumerMasterService {
    List<ConsumerCategoryMasterBean> fetchAllConsumerCategory();
    List<ConsumerRangeMasterBean> fetchConsumerRange();
    List<ConsumerViewDTO> fetchAllConsumer(String ward_id,String holding_no,String consumer_no,String consumer_name,Long mobile_no);
    Optional<ConsumerRateChartBean> fetchMonthlyAmount(String consumerRangeMstrId);

    String createConsumer(ConsumerEntryDTO consumerEntryDTO) throws SQLException;

    List<ConsumerDetailsViewDTO> fetchConsumerByConsumerNo(String consumerNo);

    List<AreaDetailsViewDTO> fetchAreaDetailByConsumerNo(String consumerNo);

    List<MonthlyRateViewDTO> fetchMonthlyRateByConsumerNo(String consumerNo);

    List<ConsumerDueDTO> fetchDeuDetailsByConsumerNo(String consumerNo);

    List<PaymentReceiptViewDTO> getPaymentReceiptByTrnNo(String transactionNo);

    List<ConsumerDemandViewDTO> getDemandByConsumerNo(String consumerNo);

    List<PaymentViewDTO> fetchPaymentDetailsByConsumerNo(String consumerNo);

    String consumerPayment(ConsumerPaymentReqDTO consumerPaymentReqDTO);

    List<WasteCounterReportDTO> userChargeCounterReport(String dateFrom, String dateTo, String wardId, String userId, String paymentMode);

    String consumerDetailsUpdate(ConsumerDetUpdateDTO consumerDetUpdateDTO);
}
