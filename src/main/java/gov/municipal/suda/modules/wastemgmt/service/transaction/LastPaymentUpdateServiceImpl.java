package gov.municipal.suda.modules.wastemgmt.service.transaction;

import gov.municipal.suda.modules.wastemgmt.dao.transaction.ConsumerDemandDao;
import gov.municipal.suda.modules.wastemgmt.dao.master.ConsumerDetailsDao;
import gov.municipal.suda.modules.wastemgmt.dao.transaction.ConsumerLastPaymentRecordDao;
import gov.municipal.suda.util.DateConversation;
import gov.municipal.suda.util.DeleteFilesAndFolder;
import gov.municipal.suda.util.common.LastPaymentUpdateMonthDropDownDto;
import gov.municipal.suda.modules.wastemgmt.dto.UsesChargeLastPaymentUpdateDto;
import gov.municipal.suda.modules.wastemgmt.dto.WasteLastPaymentUpdateDto;
import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerDetailsBean;
import gov.municipal.suda.modules.wastemgmt.model.transaction.ConsumerDemandBean;
import gov.municipal.suda.modules.wastemgmt.model.transaction.ConsumerLastPaymentBean;
import gov.municipal.suda.util.UploadFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LastPaymentUpdateServiceImpl implements LastPaymentUpdateService{
    @Autowired
    ConsumerLastPaymentRecordDao lastPaymentRecordDao;
    @Autowired
    ConsumerDemandDao demandDao;
    @Autowired
    ConsumerDetailsDao consumerDetailsDao;
	/*
	 * @Value("${aws.accessKeyId}") private String accessKey;
	 * 
	 * @Value("${aws.secretKey}") private String secretKey;
	 * 
	 * @Value("${aws.region}") private String region;
	 * 
	 * @Value("${aws.userCharge.lastPayment.file.suffix}") private String
	 * fileSuffix;
	 * 
	 * @Value("${aws.userCharge.lastPayment.bucketName}") private String bucketName;
	 */

    @Override
    public void LastPaymentUpdate(MultipartFile file,
                                  Long consumerMasterId,
                                  String receipt_no,
                                  String receipt_date,
                                  String book_no,
                                  String frm_month,
                                  String upto_month,
                                  BigDecimal tot_amount,
                                  Long user_id,
                                  String fromDate,
                                  String upToDate) throws IOException, ParseException {

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //log.info("Start Date {}",formatter.parse(dto.getFromDate()));
/*        String fromMonth=dto.getFrm_month(); // MM-YYYY (get it from first list from the drop down)
        int getLastDayFromMonth= DateConversation.getLastDayOfMonth("YYYY-MM",fromMonth);
        log.info("Last Day of the Month {}", getLastDayFromMonth);
        String concatFromMonth= getLastDayFromMonth+"-"+fromMonth;
        String finalFromMonth=DateConversation.change_DD_MM_YYYY_To_YYYY_MM_DD(concatFromMonth);
        log.info("Final From Month{}", finalFromMonth);*/
        Long consumer_details_id = consumerDetailsDao.findConsumerDetailsByConsumerMasterId(consumerMasterId).get().getId();
        if(consumer_details_id !=null) {
            StringBuilder finalFileName = new StringBuilder();
           // finalFileName.append(fileSuffix);
            finalFileName.append(consumer_details_id);
            finalFileName.append("-");
            finalFileName.append(file.getOriginalFilename());
            try {
             // //  String fileName = UploadFile.AWS_Upload(accessKey, secretKey, region, bucketName, finalFileName.toString(), file);

                ConsumerLastPaymentBean data = new ConsumerLastPaymentBean();
                data.setConsumer_details_id(consumer_details_id);
                data.setBook_no(book_no);
                data.setReceipt_date(receipt_date);
                data.setReceipt_no(receipt_no);
                ///data.setRecpt_filename(fileName);
                data.setFrm_month(frm_month);
                data.setUpto_month(upto_month);  //MM-YYYY
                data.setTot_amount(tot_amount);
                data.setUser_id(user_id);
                data.setStatus(1);
                data.setStampdate(Timestamp.valueOf(LocalDateTime.now()));
                ConsumerLastPaymentBean lastPaymentId = lastPaymentRecordDao.save(data);
                //log.info(formatter.parse(fromDate).toString());
                // log.info(formatter.parse(upToDate).toString());
                List<ConsumerDemandBean> pendingDemand = demandDao.getDemandBetweenFromAndToDateByConsumerId(lastPaymentId.getConsumer_details_id(), fromDate, upToDate);
                if (pendingDemand.size() > 0) {
                    for (ConsumerDemandBean demand : pendingDemand) {
                        ConsumerDemandBean demandData=demandDao.getOne(demand.getId());
                        //Optional<ConsumerDemandBean> updateLastPaymentDemand = demandDao.findById(demand.getId());
                        demandData.setLast_payment_id(lastPaymentId.getId());
                        demandData.setPayment_status(1L);
                        demandDao.save(demandData);
                    }
                }
            } finally {
                DeleteFilesAndFolder.deleteFileFromCurrentDirectory(file.getOriginalFilename());
            }
        }
    }

    @Override
    public List<UsesChargeLastPaymentUpdateDto> LastPaymentUpdateView(String consumerNo, String demandUpTo) {
        Optional<ConsumerDetailsBean> consumerDetails = consumerDetailsDao.findByMstrIdConsumNo(consumerNo);
        List<UsesChargeLastPaymentUpdateDto> results = new ArrayList<>();
        if(consumerDetails.isPresent()) {
            Long consumerDetailsId= consumerDetails.get().getId();
            List<ConsumerDemandBean> getAllDueDemand = demandDao.getDemandBetweenFromAndToDateByConsumerId(consumerDetailsId,demandUpTo);
            for(ConsumerDemandBean demand : getAllDueDemand) {
                UsesChargeLastPaymentUpdateDto dto =new UsesChargeLastPaymentUpdateDto();
                StringBuilder finalDemandUpTo = new StringBuilder();
                //String tempValue=demand.getDemand_to().substring(5,7);
                //log.info(tempValue);
                int monthNumber=Integer.parseInt(demand.getDemand_to().substring(5,7));
                String monthName=Month.of(monthNumber).name();
                finalDemandUpTo.append(monthName);
                finalDemandUpTo.append("-");
                finalDemandUpTo.append(demand.getDemand_to().substring(0,4));
                //log.info(finalDemandUpTo.toString());
                dto.setMonth(finalDemandUpTo.toString());
                dto.setAmount(demand.getDemand_amount());
                results.add(dto);

            }

        }
        return results;
    }

    @Override
    public List<LastPaymentUpdateMonthDropDownDto> LastPaymentUpdateMonthDropDown(String consumerNo) {
        List<LastPaymentUpdateMonthDropDownDto> results= new ArrayList<>();
        Optional<ConsumerDetailsBean> consumerDetails=consumerDetailsDao.findConsumerDetailsByConsumerNo(consumerNo);
        if(consumerDetails.isPresent()) {
            List<ConsumerDemandBean> demandList = demandDao.fetchConsumerDueDemandByDtlId(consumerDetails.get().getId());
            for(ConsumerDemandBean demand: demandList) {
                StringBuilder finalDemandUpTo = new StringBuilder();
                LastPaymentUpdateMonthDropDownDto dto = new LastPaymentUpdateMonthDropDownDto();
                String monthAfterSubString = demand.getDemand_to().substring(0,7);
                dto.setMonthId(monthAfterSubString);
                int monthNumber=Integer.parseInt(demand.getDemand_to().substring(5,7));
                String monthName=Month.of(monthNumber).name();
                finalDemandUpTo.append(monthName);
                finalDemandUpTo.append("-");
                finalDemandUpTo.append(demand.getDemand_to().substring(0,4));
                dto.setMonth(finalDemandUpTo.toString());
                dto.setFullDate(demand.getDemand_to());
                dto.setFromDate(demand.getDemand_from());
                results.add(dto);
            }
        }
        return results;
    }
}
