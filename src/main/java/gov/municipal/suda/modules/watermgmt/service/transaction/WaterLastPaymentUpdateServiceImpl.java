package gov.municipal.suda.modules.watermgmt.service.transaction;

import gov.municipal.suda.modules.watermgmt.dao.master.WaterConsumerDetailsDao;
import gov.municipal.suda.modules.watermgmt.dao.transaction.DemandDetailsWaterDao;
import gov.municipal.suda.modules.watermgmt.dao.transaction.WaterLastPaymentRecordDao;
import gov.municipal.suda.modules.watermgmt.dto.response.WaterLastPaymentUpdateMonthDropDownDto;
import gov.municipal.suda.modules.watermgmt.dto.transaction.WaterLastPaymentUpdateDto;
import gov.municipal.suda.modules.watermgmt.dto.transaction.WaterLastPaymentViewResponseDto;
import gov.municipal.suda.modules.watermgmt.dto.transaction.WaterLastPaymentViewResponseMainDto;
import gov.municipal.suda.modules.watermgmt.model.master.WaterConsumerDetailsBean;
import gov.municipal.suda.modules.watermgmt.model.transaction.DemandDetailsWaterBean;
import gov.municipal.suda.modules.watermgmt.model.transaction.WaterLastPaymentRecordBean;
import gov.municipal.suda.util.DeleteFilesAndFolder;
import gov.municipal.suda.util.UploadFile;
import gov.municipal.suda.util.common.LastPaymentUpdateMonthDropDownDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WaterLastPaymentUpdateServiceImpl implements WaterLastPaymentUpdateService{
    @Autowired
    WaterLastPaymentRecordDao lastPaymentRecordDao;
    @Autowired
    DemandDetailsWaterDao waterDao;
    @Autowired
    WaterConsumerDetailsDao waterConsumerDetailsDao;
	
	  @Value("${aws.accessKeyId}") private String accessKey;
	  
	  @Value("${aws.secretKey}") private String secretKey;
	  
	  @Value("${aws.region}") private String region;
	  
	  @Value("${aws.water.lastPayment.file.suffix}") private String fileSuffix;
	  
	  @Value("${aws.water.lastPayment.bucketName}") private String bucketName;
	 

    @Override
    public void WaterLastPaymentUpdate(MultipartFile file,Long consumer_details_id, String receipt_no, String receipt_date, String book_no, String frm_month,
                                       String upTo_month, BigDecimal tot_amount, Long user_id, String fromDate, String upToDate) throws IOException, ParseException {

        YearMonth final_upToMonth = YearMonth.parse(upTo_month,DateTimeFormatter.ofPattern("yyyy-MM"));
        log.info("Final upToMonth {} ",final_upToMonth.atEndOfMonth());
        log.info("Final FromMonth {} ", frm_month+"-01");
        StringBuilder finalFileName=new StringBuilder();
       //// finalFileName.append(fileSuffix);
        finalFileName.append("-");
        finalFileName.append(consumer_details_id);
        finalFileName.append("-");
        finalFileName.append(file.getOriginalFilename());
        try {
      ////  String fileName=UploadFile.AWS_Upload(accessKey,secretKey,region,bucketName,finalFileName.toString(),file);
        WaterLastPaymentRecordBean data=new WaterLastPaymentRecordBean();
        data.setConsumer_dets_id(consumer_details_id);
        data.setBook_no(book_no);
        data.setReceipt_date(receipt_date);
        data.setReceipt_no(receipt_no);
      ////  data.setRecpt_filename(fileName);
        data.setDue_month_from(frm_month+"-01");
        data.setDue_month_upto(final_upToMonth.atEndOfMonth().toString());
        data.setTot_amount(tot_amount);
        data.setUser_id(user_id);
        data.setEntry_date(LocalDate.now().toString());
        data.setEntry_time(LocalTime.now().toString());
        WaterLastPaymentRecordBean lastPaymentId= lastPaymentRecordDao.save(data);

        List<DemandDetailsWaterBean> pendingDemand=waterDao.getDueDemandByConsumerIdsTillUpToDate(lastPaymentId.getConsumer_dets_id(), fromDate,upToDate);
        log.info("Total Due Demand Records for the LastPayment Update in Water Module {}", pendingDemand.size());
        //log.info("from date format is {} ",formatter.parse(fromDate));
        if(pendingDemand.size()>0) {
            for(DemandDetailsWaterBean demand: pendingDemand) {
               DemandDetailsWaterBean updateLastPaymentDemand = waterDao.getOne(demand.getId());
                updateLastPaymentDemand.setLast_payment_id(lastPaymentId.getId());
                updateLastPaymentDemand.setPayment_status(1);
                waterDao.save(updateLastPaymentDemand);
            }
        }
        }
        finally {
            DeleteFilesAndFolder.deleteFileFromCurrentDirectory(file.getOriginalFilename());
        }
    }

    @Override
    public WaterLastPaymentViewResponseMainDto LastPaymentUpdateView(String consumerNo, String demandFrom, String demandUpTo) {

        DateTimeFormatter dtfInput = DateTimeFormatter.ofPattern("dd/MM/uuuu", Locale.ENGLISH);
        DateTimeFormatter dtfOutputEng = DateTimeFormatter.ofPattern("dd-MMM-uuuu", Locale.ENGLISH);
        WaterLastPaymentViewResponseMainDto result= new WaterLastPaymentViewResponseMainDto();
        List<WaterLastPaymentViewResponseDto> resultDto= new ArrayList<>();
        WaterConsumerDetailsBean consumerDetails = waterConsumerDetailsDao.findIdByConsumerNo(consumerNo);
        BigDecimal totalAmount=new BigDecimal(0.00);
        if(consumerDetails !=null) {
            List<DemandDetailsWaterBean> demandList = waterDao.getDueDemandByConsumerIdsTillUpToDate(consumerDetails.getId(),demandFrom, demandUpTo);
            if(demandList.size() > 0) {
                for(DemandDetailsWaterBean demand:demandList) {
                    WaterLastPaymentViewResponseDto dto = new WaterLastPaymentViewResponseDto();
                    String strFromDate = demand.getDemand_from();
                    String strToDate= demand.getDemand_upto();
                    LocalDate beforeFromDateFormat = LocalDate.parse(strFromDate, dtfInput);
                    LocalDate beforeToDateFormat = LocalDate.parse(strToDate,dtfInput);
                    String fromDate = dtfOutputEng.format(beforeFromDateFormat);
                    String toDate= dtfOutputEng.format(beforeToDateFormat);
                    dto.setDemandFrom(fromDate);
                    dto.setDemandTo(toDate);
                    dto.setAmount(demand.getDemand_amount());
                    totalAmount=totalAmount.add(demand.getDemand_amount());
                    resultDto.add(dto);
                }
                result.setConsumerDetailsId(consumerDetails.getId());
                result.setTotalPayment(totalAmount);
                result.setLastPayment(resultDto);
            }
        }
        return result;
    }

    @Override
    public List<WaterLastPaymentUpdateMonthDropDownDto> WaterLastPaymentUpdateDropDown(String consumerNo) {
        DateTimeFormatter dtfInput = DateTimeFormatter.ofPattern("dd/MM/uuuu", Locale.ENGLISH);
        DateTimeFormatter dtfOutputEng = DateTimeFormatter.ofPattern("dd-MMM-uuuu", Locale.ENGLISH);
        DateTimeFormatter dtfOutput1Eng = DateTimeFormatter.ofPattern("uuuu-MM-dd", Locale.ENGLISH);

        List<WaterLastPaymentUpdateMonthDropDownDto> results= new ArrayList<>();
        if(consumerNo !=null) {
            WaterConsumerDetailsBean consumerDetails = waterConsumerDetailsDao.findIdByConsumerNo(consumerNo);
            if(consumerDetails !=null) {
                List<DemandDetailsWaterBean> demandList = waterDao.getDueDemandByConsumerId(consumerDetails.getId());
                if(demandList.size()>0) {
                    for(DemandDetailsWaterBean demand: demandList) {
                        WaterLastPaymentUpdateMonthDropDownDto dto = new WaterLastPaymentUpdateMonthDropDownDto();
                        String strToDate= demand.getDemand_upto();
                        LocalDate beforeToDateFormat = LocalDate.parse(strToDate,dtfInput);
                        String toDate= dtfOutputEng.format(beforeToDateFormat);
                        String toDateIndex=dtfOutput1Eng.format(beforeToDateFormat);
                        dto.setMonthId(toDateIndex.substring(0,7));
                        dto.setMonth(toDate.substring(3));
                        dto.setFullDate(demand.getDemand_upto());
                        dto.setFromMonth(toDateIndex);
                        results.add(dto);
                    }
                }
            }
        }
        return results.stream().sorted(Comparator.comparing(WaterLastPaymentUpdateMonthDropDownDto::getMonthId)).collect(Collectors.toList());
    }

}
