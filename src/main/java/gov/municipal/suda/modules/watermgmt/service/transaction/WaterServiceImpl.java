package gov.municipal.suda.modules.watermgmt.service.transaction;

import gov.municipal.suda.exception.AlreadyExistException;
import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.exception.DatabaseException;
import gov.municipal.suda.modules.property.dao.master.FinYearDao;
import gov.municipal.suda.modules.property.dao.master.WardDao;
import gov.municipal.suda.modules.property.dto.CollectionsBodyDto;
import gov.municipal.suda.modules.property.dto.CounterCollectionReportDTO;
import gov.municipal.suda.modules.property.model.master.FinYearBean;
import gov.municipal.suda.modules.property.model.master.WardBean;
import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerDetailsBean;
import gov.municipal.suda.modules.wastemgmt.model.transaction.ConsumerDemandBean;
import gov.municipal.suda.modules.watermgmt.dao.master.*;
import gov.municipal.suda.modules.watermgmt.dao.transaction.*;
import gov.municipal.suda.modules.watermgmt.dto.response.ConsumerEntrySuccessResponse;
import gov.municipal.suda.modules.watermgmt.dto.response.ConsumerListResponse;
import gov.municipal.suda.modules.watermgmt.dto.response.ConsumerPaymentDetailsResponse;
import gov.municipal.suda.modules.watermgmt.dto.transaction.*;
import gov.municipal.suda.modules.watermgmt.model.master.*;
import gov.municipal.suda.modules.watermgmt.model.transaction.*;
import gov.municipal.suda.modules.watermgmt.repo.WaterCustomConsumerBasicSearch;
import gov.municipal.suda.modules.watermgmt.repo.WaterCustomConsumerDetailsSearch;
import gov.municipal.suda.util.*;
import gov.municipal.suda.usermanagement.dao.UserDetailsDao;
import gov.municipal.suda.usermanagement.model.UserDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.rowset.CachedRowSet;
import javax.swing.text.html.Option;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WaterServiceImpl implements WaterService{
    @Autowired
    WaterConsumerDetailsDao waterConsumerDetailsDao;
    @Autowired
    ConsumerBasicDetailsDao consumerBasicDetailsDao;
    @Autowired
    ConsumerConnectionDetailsDao consumerConnectionDetailsDao;
    @Autowired
    DemandDetailsWaterDao demandDetailsWaterDao;
    @Autowired
    ConsumerPaymentDetailsDao consumerPaymentDetailsDao;
    @Autowired
    WaterTransactionMasterDao waterTransactionMasterDao;
    @Autowired
    WaterTransactionDetailsDao waterTransactionDetailsDao;
    @Autowired
    WaterRateMasterDao waterRateMasterDao;
    @Autowired
    WaterRangeMasterDao waterRangeMasterDao;
    @Autowired
    ExtraRoomChargeDao extraRoomChargeDao;
    @Autowired
    WaterPaymentReceiptDao waterPaymentReceiptDao;
    @Autowired
    FinYearDao finYearDao;
    @Autowired
    PropertyTypeWaterDao propertyTypeWaterDao;
    @Autowired
    WardDao wardDao;
    @Autowired
    ConnectionTypeMasterDao connectionTypeMasterDao;
    @Autowired
    UserDetailsDao userDetailsDao;

    @Autowired
    WaterDemandService waterDemandService;
    @Autowired
    private WaterCustomConsumerDetailsSearch customConsumerDetailsSearch;

    @Autowired
    private WaterCustomConsumerBasicSearch customConsumerBasicSearch ;
    @Override
//    @Transactional
//            (rollbackFor = Exception.class,
//                    noRollbackFor = EntityNotFoundException.class)
    public ConsumerEntrySuccessResponse addEntry(ConsumerEntryDto consumerEntry) {
    	
    	log.info("Consurem Entry DTO ------ Line 104 ---- {}", consumerEntry);;
        WaterConsumerDetailsBean consumerDetail= new WaterConsumerDetailsBean();
        ConsumerBasicDetailsBean consumerBasicDetails = new ConsumerBasicDetailsBean();
        ConsumerConnectionDetailsBean consumerConnectionDetailsBean=new ConsumerConnectionDetailsBean();

        ConsumerEntrySuccessResponse result=new ConsumerEntrySuccessResponse();
        String newConsumerNo= GenerateUtilOfWaterModule.generateConsumerNo();
        log.info("Geenrate Consuremer Nu ------- Line 111 ---- {}",newConsumerNo);
        
        String currentFinancialYear=null;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        Long currentFinId=0L;
        FinYearBean finYearBean=null;
        int lastDayOfTheMonth= DateConversation.getLastDayOfMonth("yyyy-MM-dd",LocalDate.now().toString());
        String formattedUpToDate=dateFormatter.format(LocalDate.now()).substring(2);
        String finalUpToDate=lastDayOfTheMonth+formattedUpToDate;

        try {
            BigDecimal arrearAmount= new BigDecimal(0.00);
            if(consumerEntry.getDemandDetailsWater().getArrearAmount()!=null) {
                 arrearAmount = consumerEntry.getDemandDetailsWater().getArrearAmount();
                 log.info("arrear Amount ---- Line 125 ---- {}",arrearAmount);
            }

            if (LocalDate.now().getMonth().getValue() > 3) {
                currentFinancialYear = LocalDate.now().getYear() + "-" + Integer.valueOf(LocalDate.now().getYear() + 1);
                finYearBean = finYearDao.getFinIdByFinYear(currentFinancialYear);
                currentFinId = finYearBean.getId();
            } else if (LocalDate.now().getMonth().getValue() <= 3) {
                currentFinancialYear = Integer.valueOf(LocalDate.now().getYear() - 1) + "-" + LocalDate.now().getYear();
                finYearBean = finYearDao.getFinIdByFinYear(currentFinancialYear);
                currentFinId = finYearBean.getId();
            }
            BigDecimal rate=consumerEntry.getRate();
            log.info("Rate ---- line 138 ---- {}",rate);
            Long rateId = consumerEntry.getRateId();
            log.info("Rate Id ---- Line 140 -- {}",rateId);
            BigDecimal extraRoomCharge=consumerEntry.getExtraRoomCharge();
            log.info("Extra Rooome charge  ---- Line 142 -- {}",extraRoomCharge);
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outPutFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            if (null!= consumerEntry.getConsumerDetails().getOldConsumerNo()) {
                consumerDetail.setOld_consumer_no(consumerEntry.getConsumerDetails().getOldConsumerNo());
            } else if (null== consumerEntry.getConsumerDetails().getOldConsumerNo()) {
                consumerDetail.setOld_consumer_no(newConsumerNo);
            }
            consumerDetail.setConsumer_no(newConsumerNo);
            consumerDetail.setHolding_no(consumerEntry.getConsumerDetails().getHoldingNo());
            consumerDetail.setIs_nigam_emp(consumerEntry.getConsumerDetails().getIsNigamEmp());
            consumerDetail.setRegularization("NA");
            consumerDetail.setProp_type_id(consumerEntry.getConsumerDetails().getPropertyTypeId());
            consumerDetail.setUser_id(consumerEntry.getUserId());
            consumerDetail.setStatus(1);
            consumerDetail.setUpdate_status_ward(0L);
            consumerDetail.setUpdate_ward_user_id(0L);
            consumerDetail.setProperty_address(consumerEntry.getConsumerDetails().getPropertyAddress());
            consumerDetail.setUploaded_doc("NA");
            consumerDetail.setEntry_date(dateFormatter.format(LocalDate.now()));
            consumerDetail.setEntry_time(LocalTime.now().toString());
            consumerDetail.setWard_id(consumerEntry.getWardId());
            consumerDetail.setOld_ward_id(consumerEntry.getWardId());
            WaterConsumerDetailsBean newConsumerId = waterConsumerDetailsDao.save(consumerDetail);
            consumerBasicDetails.setName(consumerEntry.getConsumerBasicDetails().getName());
            consumerBasicDetails.setGuardian_name(consumerEntry.getConsumerBasicDetails().getGuardianName());
            consumerBasicDetails.setGuardian_name(consumerEntry.getConsumerBasicDetails().getGuardianName());
            consumerBasicDetails.setMobile_no(consumerEntry.getConsumerBasicDetails().getMobileNo());
            consumerBasicDetails.setRelation(consumerEntry.getConsumerBasicDetails().getRelation());
            consumerBasicDetails.setBpl_apl_id(1L); //need to convert a dynamic not static
            consumerBasicDetails.setUser_id(consumerEntry.getUserId());
            consumerBasicDetails.setStatus(1);
            consumerBasicDetails.setEntry_date(dateFormatter.format(LocalDate.now()));
            consumerBasicDetails.setEntry_time(LocalTime.now().toString());
            consumerBasicDetails.setConsumer_dets_id(newConsumerId.getId());
            consumerBasicDetailsDao.save(consumerBasicDetails);
            consumerConnectionDetailsBean.setConsumer_dets_id(newConsumerId.getId());
            consumerConnectionDetailsBean.setDate_of_connection(consumerEntry.getConsumerConnectionDetails().getDateOfConnection());
            consumerConnectionDetailsBean.setConn_type_id(consumerEntry.getConsumerConnectionDetails().getConnectionType());
            consumerConnectionDetailsBean.setIntial_meter_reading(0L); // need confirmation
            consumerConnectionDetailsBean.setRate_mstr_id(rateId);
            consumerConnectionDetailsBean.setNo_table_room(consumerEntry.getNoOfRoomsTableConnection().longValue());
            consumerConnectionDetailsBean.setWard_id(consumerEntry.getWardId());
            consumerConnectionDetailsBean.setOld_ward_id(consumerEntry.getWardId());
            consumerConnectionDetailsBean.setEntry_time(LocalTime.now().toString());
            consumerConnectionDetailsBean.setEntry_date(dateFormatter.format(LocalDate.now()));
            consumerConnectionDetailsBean.setStatus(1);
            consumerConnectionDetailsBean.setProperty_type_id(consumerEntry.getConsumerDetails().getPropertyTypeId());
            consumerConnectionDetailsBean.setUser_id(consumerEntry.getUserId());
            consumerConnectionDetailsBean.setMeter_no("0");
            consumerConnectionDetailsDao.save(consumerConnectionDetailsBean);
            //**********************Demand save start **********************************
            LocalDate startDate = LocalDate.of(Integer.parseInt(consumerEntry.getDemandDetailsWater().getDemandFrom().substring(0,4)),
                    Month.of(LocalDate.parse(consumerEntry.getDemandDetailsWater().getDemandFrom()).getMonthValue()), 1);
            log.info("Start Date will be----- Line 198 ------- {} ",startDate.toString());
            LocalDate endDate = LocalDate.now();
            
            if(LocalDate.now().getMonth().getValue()>3) {
                endDate = LocalDate.of(LocalDate.now().getYear() + 1, Month.MARCH, 31);
            } else if (LocalDate.now().getMonth().getValue()>0 && LocalDate.now().getMonth().getValue()<4) {
                endDate = LocalDate.of(LocalDate.now().getYear(), Month.MARCH, 31);
            }
            log.info("End date will be---- Line 205 ----- {} ",endDate.toString());
            DemandDetailsWaterBean results=new DemandDetailsWaterBean();
            List<String> demandUpToList=new ArrayList<>();
            BigDecimal grandTotalDemand = new BigDecimal(0.00);
            for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusMonths(1)) {
                BigDecimal penalty = new BigDecimal(0.00);
                DemandDetailsWaterBean demandDetails = new DemandDetailsWaterBean();
                String finYearFromDB=null;
                Long finYearIdFromDB=null;

                BigDecimal totalDemandAmount = new BigDecimal(0.00);
                YearMonth yearMonth = YearMonth.from(date);
                demandUpToList.add(yearMonth.atEndOfMonth().toString());
                demandDetails.setWard_id(consumerEntry.getWardId());
                demandDetails.setConsumer_dets_id(newConsumerId.getId());
                demandDetails.setDemand_date(dateFormatter.format(LocalDate.now()));
                demandDetails.setDemand_from(outPutFormat.format(yearMonth.atDay(1)));
                demandDetails.setDemand_upto(outPutFormat.format(yearMonth.atEndOfMonth()));
                demandDetails.setUnit_rate_id(rateId);
                demandDetails.setUnit_amount(rate); //need to monitor this one between unit rate and rate

                if(yearMonth.getMonthValue()>3) {
                    YearMonth yearMonthInside=yearMonth.plusYears(1);;
                    String financialYear= String.valueOf(yearMonthInside.getYear());
                    log.info("Current financial Year during demand Creations {} ",financialYear);
                    FinYearBean getFinYear=finYearDao.findFinYearByExtractLastYear(financialYear);
                    finYearFromDB=getFinYear.getFy_name();
                    finYearIdFromDB=getFinYear.getId();

                }
                else {
                    String financialYear= String.valueOf(yearMonth.getYear());
                    //log.info("Current financial WithOut increase Year during demand Creations {} ",financialYear);
                    FinYearBean getFinYear=finYearDao.findFinYearByExtractLastYear(financialYear);
                    finYearFromDB=getFinYear.getFy_name();
                    finYearIdFromDB=getFinYear.getId();
                }

             if(consumerEntry.getConsumerDetails().getPropertyTypeId()==3L || consumerEntry.getConsumerDetails().getPropertyTypeId() ==4L)  {

                    if(extraRoomCharge==null) {
                        totalDemandAmount = totalDemandAmount.add(rate);
                        demandDetails.setExtra_room_charge(new BigDecimal(0.00));
                    }
                    else if(extraRoomCharge!=null) {
                        totalDemandAmount = totalDemandAmount.add(rate);
                        demandDetails.setExtra_room_charge(extraRoomCharge);
                    }

                }
             else {
                 totalDemandAmount =rate.multiply(consumerEntry.getNoOfRoomsTableConnection());
                 demandDetails.setExtra_room_charge(extraRoomCharge);
             }
                if (YearMonth.from(LocalDate.now()).isAfter(yearMonth)) {
                    penalty = totalDemandAmount.multiply(new BigDecimal(0.15)).setScale(0, RoundingMode.HALF_UP);
                    totalDemandAmount = totalDemandAmount.add(penalty).setScale(0, RoundingMode.HALF_UP);
                }
                //Temp Solution ---------------------------------------------------------
                demandDetails.setPenalty(new BigDecimal(0.00));
                demandDetails.setLate_fine(penalty);
                
                if (consumerEntry.getConsumerDetails().getIsNigamEmp().equalsIgnoreCase("Yes")) {
                    totalDemandAmount = totalDemandAmount.multiply(new BigDecimal(0.5));
                }
                grandTotalDemand=grandTotalDemand.add(totalDemandAmount);
                demandDetails.setDemand_amount(totalDemandAmount);
                demandDetails.setFy_year(finYearFromDB);
                demandDetails.setFy_id(finYearIdFromDB);
                demandDetails.setPayment_status(0);
                demandDetails.setLast_payment_id(0L);
                demandDetails.setStatus(1);
                demandDetails.setEntry_date(dateFormatter.format(LocalDate.now()));
                demandDetails.setEntry_time(LocalTime.now().withNano(0).toString());
                demandDetails.setOld_ward_id(consumerEntry.getWardId());
                demandDetails.setUser_id(consumerEntry.getUserId());
                results= demandDetailsWaterDao.save(demandDetails);
            }
            if(results.getId()!=null) {
                WaterDemandPrintLogDto demandPrintLogDto = new WaterDemandPrintLogDto();
                demandUpToList.sort(Comparator.naturalOrder());
                demandPrintLogDto.setConsumer_dets_id(results.getConsumer_dets_id());
                demandPrintLogDto.setTot_amount(grandTotalDemand);
                demandPrintLogDto.setDemand_from(demandUpToList.get(0));
                demandPrintLogDto.setDemand_upto(demandUpToList.get(demandUpToList.size()-1));
                demandPrintLogDto.setWard_id(results.getWard_id());
                demandPrintLogDto.setOld_ward_id(results.getWard_id());
                demandPrintLogDto.setUser_id(consumerEntry.getUserId());
                waterDemandService.demandPrintLogEntry(demandPrintLogDto);
            }
            else if(results==null) {
                throw new BadRequestException("Demand Print Log not saved");
            }
//**********************Demand save End **********************************
//            else if(totalMonth==0) {
//            throw new BadRequestException("Demand not generated!!!!");
//            }
            result.setConsumerId(newConsumerId.getId());
            result.setConsumerNo(newConsumerNo);
            return  result;

        }
        catch(Exception e) {
            log.info("Error MEssafe -------> {}",e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }
    @Override
    public WaterPaymentReceiptDto getReceiptDuringNewConsumerEntry(String consumerNo, String modeOfPayment) {
        Long consumerId=0L;
        WaterPaymentReceiptDto result = new WaterPaymentReceiptDto();
        DateTimeFormatter dashTypeDateFormatter= DateTimeFormatter.ofPattern("dd-MM-YYYY");

       try {
           WaterConsumerDetailsBean waterConsumerBeanResult=waterConsumerDetailsDao.findIdByConsumerNo(consumerNo);
           if(waterConsumerBeanResult !=null) {
               consumerId=waterConsumerBeanResult.getId();
               WaterConsumerDetailsBean waterConsumerDetailsBean = waterConsumerDetailsDao.getOne(consumerId);
               Optional<WardBean> wardBean=wardDao.findById(waterConsumerDetailsBean.getWard_id());
               Optional<ConsumerBasicDetailsBean> consumerBasicDetailsBeanResult = consumerBasicDetailsDao.getConsumerBasicDetailsByConsumerDetailsId(consumerId);
               ConsumerBasicDetailsBean consumerBasicDetailsBean = null;
               if (consumerBasicDetailsBeanResult.isPresent()) {
                   consumerBasicDetailsBean = consumerBasicDetailsBeanResult.get();
               }

               Optional<DemandDetailsWaterBean> demandDetailsResult = demandDetailsWaterDao.getDemandByPaymentStatusDuringNewEntry(consumerId);
               DemandDetailsWaterBean demandDetailsWaterBean = null;
               if (demandDetailsResult.isPresent()) {
                   demandDetailsWaterBean = demandDetailsResult.get();
               }

               Optional<WaterTransactionDetailsBean> waterTransactionDetailResult = waterTransactionDetailsDao.getCheckAndDDByClearanceStatus(consumerId);
               WaterTransactionDetailsBean waterTransactionDetailsBean = null;
               if (waterTransactionDetailResult.isPresent()) {
                   waterTransactionDetailsBean = waterTransactionDetailResult.get();
               }

               Optional<ConsumerPaymentDetailsBean> consumerPaymentDetailsResult = consumerPaymentDetailsDao
                       .getConsumerPaymentDetailsByDemandAndConsumerDetailsIdLong(demandDetailsWaterBean.getId(), consumerId);
               ConsumerPaymentDetailsBean consumerPaymentDetailsBean = null;
               if (consumerPaymentDetailsResult.isPresent()) {
                   consumerPaymentDetailsBean = consumerPaymentDetailsResult.get();
               }
               Optional<WaterPaymentReceiptBean> waterPaymentReceiptResult = waterPaymentReceiptDao
                       .getPaymentReceiptByConsumerIdAndTransactionId(consumerId, consumerPaymentDetailsBean.getTransaction_id());
               WaterPaymentReceiptBean waterPaymentReceiptBean = null;
               if (waterPaymentReceiptResult.isPresent()) {
                   waterPaymentReceiptBean = waterPaymentReceiptResult.get();
               }
               LocalDate currentDate = LocalDate.now();
               LocalTime currentTime = LocalTime.now();
               StringBuilder receiptNo = new StringBuilder();
               receiptNo.append(consumerNo);
               receiptNo.append(currentDate.getDayOfMonth());
               receiptNo.append(currentDate.getMonthValue());
               receiptNo.append(currentDate.getYear());
               receiptNo.append(currentTime.getHour());
               receiptNo.append(currentTime.getMinute());
               receiptNo.append(currentTime.getSecond());
               result.setReceiptNo(receiptNo.toString());
               log.info("New Receipt No =>", receiptNo);
               result.setDate(dashTypeDateFormatter.format(currentDate));
               result.setDepartmentSection("Water Supply Department");
               result.setAccountDescription("Water Tax and Other Tax");
               result.setWardId(wardBean.get().getWard_name());
               result.setPropertyNo(waterConsumerDetailsBean.getHolding_no());
               result.setConsumerNo(waterConsumerDetailsBean.getConsumer_no());
               result.setConsumerName(consumerBasicDetailsBean.getName());
               result.setRelation(consumerBasicDetailsBean.getRelation());
               result.setMobileNo(consumerBasicDetailsBean.getMobile_no());
               result.setGuardianName(consumerBasicDetailsBean.getGuardian_name());
               result.setPayableAmount(demandDetailsWaterBean.getDemand_amount());
               result.setDescription("Water Tax (Consumer Receipt)");
               result.setReceivableAmount(demandDetailsWaterBean.getDemand_amount());
               result.setTotal(demandDetailsWaterBean.getDemand_amount());
               result.setPenaltyAmount(demandDetailsWaterBean.getPenalty());
               result.setTotalPeriod(waterPaymentReceiptBean.getDue_months().toString());
               if (modeOfPayment.equalsIgnoreCase("CASH")) {
                   result.setModeOfPayment("CASH");
               } else if (modeOfPayment.equalsIgnoreCase("Cheque")) {
                   result.setModeOfPayment("Cheque");
                   result.setBankName(waterTransactionDetailsBean.getBank_name());
                   result.setBranchLocation(waterTransactionDetailsBean.getBranch_name());
                   result.setChequeNo(waterTransactionDetailsBean.getChq_dd_no());
                   result.setChequeDate(waterTransactionDetailsBean.getChq_dd_dte());

               } else if (modeOfPayment.equalsIgnoreCase("DD")) {
                   result.setModeOfPayment("DD");
                   result.setBankName(waterTransactionDetailsBean.getBank_name());
                   result.setChequeNo(waterTransactionDetailsBean.getChq_dd_no());
                   result.setBranchLocation(waterTransactionDetailsBean.getBranch_name());
                   result.setChequeDate(waterTransactionDetailsBean.getChq_dd_dte());

               }
           } else if (waterConsumerBeanResult==null) {
               throw new BadRequestException("Consumer No not found");

           }
       }
       catch(Exception e) {
           throw new BadRequestException(e.getMessage());
       }

        return result;
    }

    @Override
    public List<ConsumerListResponse> getConsumerList(Long wardId, String propertyNo, String consumerNo, Long mobileNo, String consumerName) {
        List<ConsumerListResponse> response = new ArrayList<>();
        List<WaterConsumerDetailsBean> consumerDetailsList=new ArrayList<>();
        List<ConsumerBasicDetailsBean> consumerBasicList=new ArrayList<>();

        if(wardId!=null || !propertyNo.isEmpty() || !consumerNo.isEmpty()) {
            consumerDetailsList = customConsumerDetailsSearch.findWaterConsumerDetailsBy(wardId, consumerNo, propertyNo);
            if (!consumerDetailsList.isEmpty()) {
                for (WaterConsumerDetailsBean consumerDetails : consumerDetailsList) {
                    Optional<ConsumerBasicDetailsBean> consumer = consumerBasicDetailsDao.getConsumerBasicDetailsByConsumerDetailsId(consumerDetails.getId());
                    Optional<WardBean> wardBean = wardDao.findById(consumerDetails.getWard_id());
                    ConsumerListResponse setResponse = new ConsumerListResponse();
                    setResponse.setConsumerNo(consumerDetails.getConsumer_no());
                    setResponse.setPropertyNo(consumerDetails.getHolding_no());
                    setResponse.setAddress(consumerDetails.getProperty_address());
                    if (consumer.isPresent()) {
                        setResponse.setConsumerName(consumer.get().getName());
                        setResponse.setMobileNo(consumer.get().getMobile_no());
                    }
                    if (wardBean.isPresent()) {
                        setResponse.setWardNo(wardBean.get().getWard_name());
                    }
                    response.add(setResponse);
                }
            }
        }

        if(mobileNo!=null || !consumerName.isEmpty()) {
            consumerBasicList=customConsumerBasicSearch.findConsumerBasicDetailsByMobileAndConsumerName(consumerName,mobileNo);
            if(!consumerBasicList.isEmpty()){
             for(ConsumerBasicDetailsBean consumerBasicDetails : consumerBasicList){
                 Optional<WaterConsumerDetailsBean> consumerDetails = waterConsumerDetailsDao.findById(consumerBasicDetails.getConsumer_dets_id());
                 Optional<WardBean> wardBean = wardDao.findById(consumerDetails.get().getWard_id());
                 ConsumerListResponse setResponse = new ConsumerListResponse();
                 if(consumerDetails.isPresent()) {
                     setResponse.setConsumerNo(consumerDetails.get().getConsumer_no());
                     setResponse.setPropertyNo(consumerDetails.get().getHolding_no());
                     setResponse.setAddress(consumerDetails.get().getProperty_address());
                 }
                 setResponse.setConsumerName(consumerBasicDetails.getName());
                 setResponse.setMobileNo(consumerBasicDetails.getMobile_no());

                 if (wardBean.isPresent()) {
                     setResponse.setWardNo(wardBean.get().getWard_name());
                 }
                 response.add(setResponse);

             }
            }
        }

        if(!consumerDetailsList.isEmpty() && !consumerBasicList.isEmpty()){
            response.clear();
            Set<Long> consumerIdsFromConsumerDetails = new HashSet<>();
            Set<Long> consumerIdsFromConsumerBasicDetails= new HashSet<>();
            for(WaterConsumerDetailsBean consumerDetailsBean: consumerDetailsList){
                consumerIdsFromConsumerDetails.add(consumerDetailsBean.getId());
            }
            for(ConsumerBasicDetailsBean consumerBasicDetails: consumerBasicList){
                consumerIdsFromConsumerBasicDetails.add(consumerBasicDetails.getConsumer_dets_id());
            }
           consumerIdsFromConsumerDetails.retainAll(consumerIdsFromConsumerBasicDetails);
            for(Long consumerDetailsIds : consumerIdsFromConsumerDetails) {
                ConsumerListResponse setResponse = new ConsumerListResponse();
                Optional<WaterConsumerDetailsBean> consumerDetails= waterConsumerDetailsDao.findById(consumerDetailsIds);
                Optional<ConsumerBasicDetailsBean> consumerBasicDetailsList = consumerBasicDetailsDao.getConsumerBasicDetailsByConsumerDetailsId(consumerDetailsIds);
                Optional<WardBean> wardBean=Optional.empty();
                if(consumerDetails.isPresent()) {
                    wardBean=wardDao.findById(consumerDetails.get().getWard_id());
                    setResponse.setConsumerNo(consumerDetails.get().getConsumer_no());
                    setResponse.setPropertyNo(consumerDetails.get().getHolding_no());
                    setResponse.setAddress(consumerDetails.get().getProperty_address());
                }
                if(consumerBasicDetailsList.isPresent()) {
                    setResponse.setConsumerName(consumerBasicDetailsList.get().getName());
                    setResponse.setMobileNo(consumerBasicDetailsList.get().getMobile_no());
                }
                if (wardBean.isPresent()) {
                    setResponse.setWardNo(wardBean.get().getWard_name());
                }
                response.add(setResponse);
            }
           // log.info("common DetailsIds", consumerIdsFromConsumerDetails);

        }

            return response;
    }

    @Override
    public ConsumerUpdateDto view(String consumerNo) {
        Long consumerDetailsId=0L;

        WaterConsumerDetailsBean consumerDetails=waterConsumerDetailsDao.findIdByConsumerNo(consumerNo);
        if(consumerDetails==null) {
            throw new BadRequestException("Consumer Details Bean with Consumer number not found");
        }
        ConsumerUpdateDto response = new ConsumerUpdateDto();
        DateTimeFormatter dashTypeDateFormatter= DateTimeFormatter.ofPattern("dd-MM-YYYY");
        LocalDate effectiveDate=null;
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outPutFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            if (consumerDetails != null) {
                consumerDetailsId = consumerDetails.getId();
                Optional<WaterConsumerDetailsBean> consumerDetailsBean = waterConsumerDetailsDao.findById(consumerDetailsId);
                if(consumerDetailsBean.isEmpty()) {
                    throw new BadRequestException("Consumer Details Not found");
                }
                Optional<ConsumerBasicDetailsBean> consumerBasicDetailsBean = consumerBasicDetailsDao.getConsumerBasicDetailsByConsumerDetailsId(consumerDetailsId);
                if(consumerBasicDetailsBean.isEmpty()) {
                    throw new BadRequestException("Consumer Basic Details Not found");
                }
                Optional<ConsumerConnectionDetailsBean> consumerConnectionDetailsBean = consumerConnectionDetailsDao.fetchConsumerConnectionByConsumerDetailsId(consumerDetailsId);
                if(consumerConnectionDetailsBean.isEmpty()) {
                    throw new BadRequestException("Consumer Connection Details Not found");
                }
                List<DemandDetailsWaterBean> dueDemands = demandDetailsWaterDao.getDueDemandByConsumerId(consumerDetailsId);
//                if(dueDemands.isEmpty()) {
//                    throw new BadRequestException("Due demand not found");
//                }
                List<DemandDetailsWaterBean> lastPaymentDay=demandDetailsWaterDao.getLastPaymentDay(consumerDetailsId);
//                if(lastPaymentDay.isEmpty()) {
//                    throw new BadRequestException("Last payment day not found");
//                }
                String maxEffectiveDate=null;
                if(lastPaymentDay.size() > 0) {
//                    effectiveDate=lastPaymentDay.stream().map(v->LocalDate.parse(v.getDemand_upto(), DateTimeFormatter.ofPattern("dd/MM/uuuu")))
//                            .max(LocalDate::compareTo).get();
                    effectiveDate= LocalDate.parse(lastPaymentDay.get(0).getDemand_upto(), DateTimeFormatter.ofPattern("dd/MM/uuuu"));
                    maxEffectiveDate=LocalDate.parse(effectiveDate.toString(),inputFormat).format(outPutFormat);
                    response.setMaxEffectFrom(maxEffectiveDate);
                }
                BigDecimal totalDemand = new BigDecimal(0.00);
                BigDecimal totalPenalty= new BigDecimal(0.00);
             
//                if(dueDemands.isEmpty()) {
//                    throw new BadRequestException("Consumer has no dues");
//                }
                if (consumerDetailsBean.isPresent()) {
                    Optional<PropertyTypeWaterMasterBean> propertyTypeBean = propertyTypeWaterDao.findById(consumerDetailsBean.get().getProp_type_id());
                    Optional<WardBean> wardBean = wardDao.findById(consumerDetailsBean.get().getWard_id());
                    if(propertyTypeBean.isEmpty()) {
                        throw new BadRequestException("Property Type Not Found");
                    }
                    if(wardBean.isEmpty()) {
                        throw new BadRequestException("Ward Number not found");
                    }
                    response.setConsumerNo(consumerDetailsBean.get().getConsumer_no());
                    response.setPropertyAddress(consumerDetailsBean.get().getProperty_address());
                    response.setOldConsumerNo(consumerDetailsBean.get().getOld_consumer_no());
                    response.setPropertyNo(consumerDetailsBean.get().getHolding_no());
                    response.setPropertyType(propertyTypeBean.get().getProp_type());
                    response.setPropertyTypeId(propertyTypeBean.get().getId());
                    response.setWardNo(wardBean.get().getWard_name());
                }
                if (consumerBasicDetailsBean.isPresent()) {
                    response.setMobileNo(consumerBasicDetailsBean.get().getMobile_no());
                    response.setConsumerName(consumerBasicDetailsBean.get().getName());
                    response.setGuardianName(consumerBasicDetailsBean.get().getGuardian_name());
                    response.setRelation(consumerBasicDetailsBean.get().getRelation());
                }

                if (consumerConnectionDetailsBean.isPresent()) {
                    Optional<ConnectionTypeMasterBean> connectionTypeMasterBean = connectionTypeMasterDao.findById(consumerConnectionDetailsBean.get().getConn_type_id());
                    if(connectionTypeMasterBean.isEmpty()) {
                        throw new BadRequestException("Connection Type Not Found");
                    }
                    response.setConnectionType(connectionTypeMasterBean.get().getConn_type());
                    response.setNoOfConnection(consumerConnectionDetailsBean.get().getNo_table_room());
                    response.setMeterNo(consumerConnectionDetailsBean.get().getMeter_no());
                    response.setInitialMeterReading(consumerConnectionDetailsBean.get().getIntial_meter_reading());
                    response.setDateOfConnection(consumerConnectionDetailsBean.get().getDate_of_connection());
                }
                if (dueDemands.size() > 0) {

                    List<ConsumerUnitRateDetailsDto> consumerUnitRateDetails= new ArrayList<>();
                    List<Long> demandIds= new ArrayList<>();
                    for (DemandDetailsWaterBean demand : dueDemands) {
                        ConsumerUnitRateDetailsDto consumerUnitRate= new ConsumerUnitRateDetailsDto();
                        totalPenalty=totalPenalty.add(demand.getPenalty().setScale(2,RoundingMode.CEILING));
                        totalDemand = totalDemand.add(demand.getDemand_amount()).setScale(2, RoundingMode.CEILING);
                        consumerUnitRate.setExtraCharge(demand.getExtra_room_charge());
                        consumerUnitRate.setEffectFrom(demand.getDemand_from());
                        consumerUnitRate.setStatus("Current");
                        consumerUnitRate.setUnitRate(demand.getUnit_amount());
                        consumerUnitRate.setLate_fine(demand.getLate_fine());
                        consumerUnitRateDetails.add(consumerUnitRate);
                        demandIds.add(demand.getId());
                    }
                    response.setDemandAmount(totalDemand);
                    response.setPenalty(totalPenalty);
                    
                    response.setTotalPayableAmount(totalDemand);
                    response.setConsumerUnitRateDetails(consumerUnitRateDetails);
                    response.setDemandId(demandIds);
                   

                }
            }
            else if(consumerDetails == null) {
                throw new BadRequestException("Consumer Details not Found");
            }
        }
        catch(Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        return response;
    }

    @Override
    public String viewDemandAmount(String fromDate, String toDate, BigDecimal arrearAmount
    , String propertyType, Long noOfRooms, String connectionType) {
        Long propertyTypeId=0L;
        Long connectionTypeId=0L;
        Long rangeId=0L;
        BigDecimal extraRoomCharge = new BigDecimal(0.00);
        int extraRoomCount=0;

        if(fromDate ==null || toDate ==null ||
                propertyType ==null || noOfRooms==null || noOfRooms==0L || connectionType==null) {
            throw new BadRequestException("Some of or all parameters can't be blank");
        }
        else if(arrearAmount==null) {
            arrearAmount=new BigDecimal(0.00);
        }

        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outPutFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        BigDecimal totalDemandAmount = new BigDecimal(0.00);
        try {

            LocalDate startDate = LocalDate.parse(fromDate, inputFormat);
            LocalDate endDate = LocalDate.parse(toDate, inputFormat);
            long totalPeriod = ChronoUnit.MONTHS.between(
                    YearMonth.from(startDate),
                    YearMonth.from(endDate)
            );
            if(totalPeriod == 0L) {
                totalPeriod=1L;
            }

            String doe = "1965-04-01";
            //if (safarvDetailsDTO.getConstruction_type().equalsIgnoreCase("VACANT")) {
            //if (startDate.getYear() <= 2010) {
            // doe = "1999-04-01";
            //}
//            else if (convertedToIntFromYear == 2011) {
//                doe = "2011-04-01";
//            } else if (convertedToIntFromYear >= 2012 && convertedToIntFromYear <= 2015) {
//                doe = "2012-04-01";
//            } else if (convertedToIntFromYear >= 2016 && convertedToIntFromYear <= 2019) {
//                doe = "2016-04-01";
//            } else if (convertedToIntFromYear >= 2020 && convertedToIntFromYear <= Integer.parseInt(entry_fy_name.substring(5, 9))) {
//                doe = "2020-04-01";
            //}
            Optional<WaterRateMasterBean> rate = null;
            Optional<WaterRangeMasterBean> range = null;
            PropertyTypeWaterMasterBean propertyTypeBean = propertyTypeWaterDao.getWaterPropertyTypeIdByPropType(propertyType);
            propertyTypeId = propertyTypeBean.getId();
            ConnectionTypeMasterBean connectionTypeBean = connectionTypeMasterDao.getConnectionTypeIdByConnectionType(connectionType);
            connectionTypeId = connectionTypeBean.getId();
            if (propertyTypeId.intValue() == 3 || propertyTypeId.intValue() == 4) {
                if (propertyTypeId.intValue() == 3L
                        && noOfRooms.intValue() >5) {
                    ExtraRoomChargeBean extraChargeResult = extraRoomChargeDao.getExtraRoomChargeByFixedPropId();
                    extraRoomCharge = extraChargeResult.getAmount();
                    extraRoomCount=noOfRooms.intValue()-5;
                    extraRoomCharge=extraRoomCharge.multiply(new BigDecimal(extraRoomCount));
                }

                range = waterRangeMasterDao.getRange(propertyTypeId, noOfRooms);
                rangeId = range.get().getId();
                rate = waterRateMasterDao.getRateWithRangeAndEffectDate(propertyTypeId, connectionTypeId, rangeId, doe);
            } else {
                rate = waterRateMasterDao.getRateWithOutRange(propertyTypeId, connectionTypeId);
            }

            if (!rate.isEmpty()) {
                totalDemandAmount = totalDemandAmount.add(rate.get().getAmount()).multiply(new BigDecimal(noOfRooms))
                        .multiply(new BigDecimal(totalPeriod)).add(arrearAmount).add(extraRoomCharge).setScale(2, RoundingMode.CEILING);
            }
        }
        catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
       return totalDemandAmount.toString();
    }

    @Override
    @Transactional
            (rollbackFor = Exception.class,
                    noRollbackFor = EntityNotFoundException.class)
    public void singleDemandGenerate(GenerateDemandDto dto) {
        Long consumerDetailsId = 0L;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outPutFormat = DateTimeFormatter.ofPattern("dd/MM/uuuu");

        Optional<WardBean> wardDetails = wardDao.findZoneIdByWardName(dto.getWardNo());
        WaterConsumerDetailsBean consumerDetailsBean = waterConsumerDetailsDao.findIdByConsumerNoAndWardId(dto.getConsumerNo(),wardDetails.get().getId());

        if (consumerDetailsBean != null) {
            consumerDetailsId = consumerDetailsBean.getId();
            try {
                List<FinYearBean> allFinYear = finYearDao.findAll();
//                if(dto.getEffectFrom()==null) {
//                    throw new BadRequestException("Demand Effect From Date can't be null");
//                }
                DemandDetailsWaterBean demandDetailsWaterBean = demandDetailsWaterDao.getDemandByConsumerIdAndDemandUpTo(consumerDetailsId, dto.getEffectFrom());
                List<DemandDetailsWaterBean> checkDemandGenerate = demandDetailsWaterDao.getLastPaymentDay(consumerDetailsId);
                LocalDate currentDemandUpto = LocalDate.parse(checkDemandGenerate.get(0).getDemand_upto(),DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                LocalDate demandFrom=currentDemandUpto.plusMonths(1);
                LocalDate startDate = LocalDate.of(Integer.parseInt(String.valueOf(demandFrom).substring(0,4)), Month.of(demandFrom.getMonthValue()), 1);
                //log.info("Start Date will be {} ",startDate.toString());
                LocalDate endDate = LocalDate.now();
                if(LocalDate.now().getMonth().getValue()>3) {
                    endDate = LocalDate.of(LocalDate.now().getYear() + 1, Month.MARCH, 31);
                } else if (LocalDate.now().getMonth().getValue()>0 && LocalDate.now().getMonth().getValue()<4) {
                    endDate = LocalDate.of(LocalDate.now().getYear(), Month.MARCH, 31);
                }
                //log.info("End date will be {} ",endDate.toString());
                if (Integer.parseInt(currentDemandUpto.toString().substring(0, 4)) >= Integer.parseInt(allFinYear.get(allFinYear.size() - 1).getFy_name().substring(5, 9))) {
                    throw new BadRequestException("Already Generated");
                }

                if (demandDetailsWaterBean != null) {
                    BigDecimal rate=demandDetailsWaterBean.getUnit_amount();
                    Long rateId= demandDetailsWaterBean.getUnit_rate_id();
                    DemandDetailsWaterBean results=null;
                    List<String> demandUpToList=new ArrayList<>();
                    BigDecimal grandTotalDemand= new BigDecimal(0.00);
                    for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusMonths(1)) {
                        BigDecimal penalty = new BigDecimal(0.00);
                        DemandDetailsWaterBean demandDetails = new DemandDetailsWaterBean();
                        String finYearFromDB=null;
                        Long finYearIdFromDB=null;

                        BigDecimal totalDemandAmount = new BigDecimal(0.00);
                        YearMonth yearMonth = YearMonth.from(date);
                        demandUpToList.add(yearMonth.atEndOfMonth().toString());
                        demandDetails.setWard_id(wardDetails.get().getId());
                        demandDetails.setConsumer_dets_id(consumerDetailsId);
                        demandDetails.setDemand_date(outPutFormat.format(LocalDate.now()));
                        demandDetails.setDemand_from(outPutFormat.format(yearMonth.atDay(1)));
                        demandDetails.setDemand_upto(outPutFormat.format(yearMonth.atEndOfMonth()));
                        demandDetails.setUnit_rate_id(rateId);
                        demandDetails.setUnit_amount(rate); //need to monitor this one between unit rate and rate


                        if(yearMonth.getMonthValue()>3) {
                            YearMonth yearMonthInside=yearMonth.plusYears(1);;
                            String financialYear= String.valueOf(yearMonthInside.getYear());
                            //log.info("Current financial Year during demand Creations {} ",financialYear);
                            FinYearBean getFinYear=finYearDao.findFinYearByExtractLastYear(financialYear);
                            finYearFromDB=getFinYear.getFy_name();
                            finYearIdFromDB=getFinYear.getId();

                        }
                        else {
                            String financialYear= String.valueOf(yearMonth.getYear());
                            //log.info("Current financial WithOut increase Year during demand Creations {} ",financialYear);
                            FinYearBean getFinYear=finYearDao.findFinYearByExtractLastYear(financialYear);
                            finYearFromDB=getFinYear.getFy_name();
                            finYearIdFromDB=getFinYear.getId();
                        }

                            totalDemandAmount = demandDetailsWaterBean.getDemand_amount();
                            demandDetails.setExtra_room_charge(demandDetailsWaterBean.getExtra_room_charge());

                        if (YearMonth.from(LocalDate.now()).isAfter(yearMonth)) {
                            penalty = totalDemandAmount.multiply(new BigDecimal(0.05)).setScale(2, RoundingMode.CEILING);
                            totalDemandAmount = totalDemandAmount.add(penalty).setScale(2, RoundingMode.CEILING);
                        }
                        grandTotalDemand=grandTotalDemand.add(totalDemandAmount);
                        demandDetails.setPenalty(penalty);
                        demandDetails.setDemand_amount(totalDemandAmount);
                        demandDetails.setFy_year(finYearFromDB);
                        demandDetails.setFy_id(finYearIdFromDB);
                        demandDetails.setPayment_status(0);
                        demandDetails.setLast_payment_id(0L);
                        demandDetails.setStatus(1);
                        demandDetails.setEntry_date(dateFormatter.format(LocalDate.now()));
                        demandDetails.setEntry_time(LocalTime.now().withNano(0).toString());
                        demandDetails.setOld_ward_id(wardDetails.get().getId());
                        demandDetails.setUser_id(demandDetailsWaterBean.getUser_id());
                        demandDetails.setTad_update(0);
                        demandDetails.setCurrent_meter_reading(0L);
                        results=demandDetailsWaterDao.save(demandDetails);
                    }
                    if(results.getId()!=null) {
                        WaterDemandPrintLogDto demandPrintLogDto = new WaterDemandPrintLogDto();
                        demandUpToList.sort(Comparator.naturalOrder());
                        demandPrintLogDto.setConsumer_dets_id(consumerDetailsId);
                        demandPrintLogDto.setTot_amount(grandTotalDemand);
                        demandPrintLogDto.setDemand_from(demandUpToList.get(0));
                        demandPrintLogDto.setDemand_upto(demandUpToList.get(demandUpToList.size()-1));
                        demandPrintLogDto.setWard_id(results.getWard_id());
                        demandPrintLogDto.setOld_ward_id(results.getWard_id());
                        demandPrintLogDto.setUser_id(dto.getUserId());
                        waterDemandService.demandPrintLogEntry(demandPrintLogDto);
                    }
                    else if(results==null) {
                        throw new BadRequestException("Demand Print Log not saved");
                    }


                } else if (demandDetailsWaterBean == null) {
                    throw new BadRequestException("Due demand details not found with existing consumer number and effect from date");
                }

            }
            catch(Exception e) {
                throw new BadRequestException(e.getMessage());
            }
        }
        else if(consumerDetailsBean==null) {
            throw new BadRequestException("Consumer Not found");
        }

    }

    @Override
    @Transactional
            (rollbackFor = Exception.class,
                    noRollbackFor = EntityNotFoundException.class)
    public void waterPayment(WaterSinglePaymentRequestDto dto) {

        if(dto!=null) {

            DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter outPutFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter dashTypeDateFormatter= DateTimeFormatter.ofPattern("dd-MM-YYYY");
            Long consumerDetailsId=0L;
            Long wardId=0L;
            Long oldWardId=0L;
            Long connectionTypeId=0L;
           try {
               WaterConsumerDetailsBean consumerDetails=waterConsumerDetailsDao.findIdByConsumerNo(dto.getConsumerNo());
               consumerDetailsId=consumerDetails.getId();
               if(consumerDetails !=null) {
                   DemandDetailsWaterBean demand = demandDetailsWaterDao.getDemandByConsumerIdAndDemandUpTo(consumerDetailsId,dto.getDateOfEffect());
                  log.info("Demand Details Water Bean: {}", Objects.toString(demand));
                   if(demand ==null) {
                       throw new BadRequestException("Demand Details Not found with current consumer no and effective date");
                   }

                   Optional<WardBean> wardBean=wardDao.findZoneIdByWardName(dto.getWardNo());
                   Optional<WardBean> wardBeanForOldId=wardDao.findZoneIdByWardName(dto.getOldWardNo());
                   if(wardBean.isEmpty()) {
                       throw  new BadRequestException("Ward Number doesn't exist");
                   }
                   wardId=wardBean.get().getId();

                   if(wardBeanForOldId.isEmpty()) {
                       oldWardId=  wardBean.get().getId();
                   }
                   else if(wardBeanForOldId.isPresent()) {
                       oldWardId = wardBeanForOldId.get().getId();
                   }
                   ConnectionTypeMasterBean connectionTypeMasterBean=connectionTypeMasterDao.getConnectionTypeIdByConnectionType(dto.getConnectionType());
                   if(connectionTypeMasterBean==null) {
                       throw new BadRequestException("Connection Type not found");
                   }
                   connectionTypeId=connectionTypeMasterBean.getId();
                   String currentDate= LocalDate.now().format(outPutFormat);
                   WaterTransactionMasterBean transactionMaster=new WaterTransactionMasterBean();
                   WaterTransactionDetailsBean transactionDetails=new WaterTransactionDetailsBean();
                   consumerDetailsId = consumerDetails.getId();
                   transactionMaster.setConsumer_dets_id(consumerDetailsId);
                   Long newId= waterTransactionMasterDao.getMaxTransactionId();
                   if(newId==null) {
                       throw new BadRequestException("TransactionId not generated");
                   }
                   String generatedTransactionNo= Generator.generateTransactionNumber(newId);
                   transactionMaster.setTransaction_no(generatedTransactionNo);
                   transactionMaster.setTransaction_date(currentDate);
                   transactionMaster.setPenalty(dto.getPenalty());
                   transactionMaster.setPayment_mode(dto.getPaymentMode());
                   transactionMaster.setWard_id(wardId);
                   transactionMaster.setUser_id(dto.getUserId());
                   transactionMaster.setProperty_type_id(dto.getPropertyTypeId());
                   transactionMaster.setConn_type_id(connectionTypeId);
                   transactionMaster.setPayable_amount(dto.getPayableAmt());
                   transactionMaster.setDemand_amount(dto.getDemandPayment());
                   transactionMaster.setIp_address(dto.getIpAddress());
                   transactionMaster.setOld_ward_id(oldWardId);
                   transactionMaster.setRemarks(dto.getNarration());
                   //transactionMaster.setVerify_date(null);
                   //transactionMaster.setCash_verify_by(null);
                   transactionMaster.setEntry_date(LocalDate.now().format(outPutFormat));
                   transactionMaster.setEntry_time(LocalTime.now().withNano(0).toString());
                   if(dto.getPaymentMode().equalsIgnoreCase("Cheque")
                           || dto.getPaymentMode().equalsIgnoreCase("DD")) {
                       transactionMaster.setCancel_status(1);
                       transactionMaster.setCash_verify_status(1);
                       transactionMaster.setVerify_date(LocalDate.now().format(outPutFormat));
                       transactionMaster.setCash_verify_by(dto.getUserId());
                   }
                   else if(dto.getPaymentMode().equalsIgnoreCase("Cash")) {
                       transactionMaster.setCancel_status(1);
                       transactionMaster.setCash_verify_status(0);
                       transactionMaster.setVerify_date(null);
                       transactionMaster.setCash_verify_by(null);
                   }
                   else if(dto.getPaymentMode().equalsIgnoreCase("Card")) {
                       transactionMaster.setCancel_status(1);
                       transactionMaster.setCash_verify_status(1);
                       transactionMaster.setVerify_date(LocalDate.now().format(outPutFormat));
                       transactionMaster.setCash_verify_by(dto.getUserId());
                   }
                   else if(dto.getPaymentMode().equalsIgnoreCase("NEFT")
                               || dto.getPaymentMode().equalsIgnoreCase("RTGS")) {
                           transactionMaster.setCancel_status(1);
                           transactionMaster.setCash_verify_status(1);
                           transactionMaster.setVerify_date(LocalDate.now().format(outPutFormat));
                           transactionMaster.setCash_verify_by(dto.getUserId());
                       }

                  /* else {
                       transactionMaster.setCancel_status(0);
                       transactionMaster.setCash_verify_status(1);
                       transactionMaster.setVerify_date(LocalDate.now().format(outPutFormat));
                       transactionMaster.setCash_verify_by(dto.getUserId());
                   }*/


                   WaterTransactionMasterBean results=waterTransactionMasterDao.save(transactionMaster);


//                   if(!dto.getPaymentMode().equalsIgnoreCase("Cheque")
//                           || !dto.getPaymentMode().equalsIgnoreCase("DD")) {
                       for (Long demandId : dto.getDemandId()) {
                           DemandDetailsWaterBean updateLastPaymentDetails = demandDetailsWaterDao.getOne(demandId);
                           updateLastPaymentDetails.setLast_payment_id(0L);
                           updateLastPaymentDetails.setPayment_status(1);
                           demandDetailsWaterDao.save(updateLastPaymentDetails);

                           ConsumerPaymentDetailsBean paymentDetails = new ConsumerPaymentDetailsBean();
                           paymentDetails.setTransaction_id(results.getId());
                           paymentDetails.setConsumer_dets_id(consumerDetailsId);
                           paymentDetails.setEntry_date(dashTypeDateFormatter.format(LocalDate.now()));
                           paymentDetails.setEntry_time(LocalTime.now().withNano(0).toString());
                           paymentDetails.setPenalty(updateLastPaymentDetails.getPenalty());
                           paymentDetails.setDemand_id(demandId);
                           paymentDetails.setMonthly_amount(updateLastPaymentDetails.getUnit_amount());
                           paymentDetails.setWard_id(updateLastPaymentDetails.getWard_id());
                           paymentDetails.setOld_ward_id(updateLastPaymentDetails.getOld_ward_id());
                           paymentDetails.setUpto_date(updateLastPaymentDetails.getDemand_upto());
                           paymentDetails.setFrom_date(updateLastPaymentDetails.getDemand_from());
                           paymentDetails.setFy_year(updateLastPaymentDetails.getFy_year());
                           paymentDetails.setFy_id(updateLastPaymentDetails.getFy_id());
                           paymentDetails.setUser_id(dto.getUserId());
                           paymentDetails.setPayment_type(dto.getPaymentMode());
                           ConsumerPaymentDetailsBean paymentDetailsResult=consumerPaymentDetailsDao.save(paymentDetails);
                           log.info("paymentDetailsResult ---- {}",paymentDetailsResult);
                       }
                  // }
                   if(dto.getPaymentMode().equalsIgnoreCase("Cheque")
                   || dto.getPaymentMode().equalsIgnoreCase("DD")) {

                /*   if(dto.getPaymentMode().equalsIgnoreCase("Cheque")
                           || dto.getPaymentMode().equalsIgnoreCase("DD")) {*/

                       transactionDetails.setBank_name(dto.getBankName());
                       transactionDetails.setBranch_name(dto.getBranch());
                       transactionDetails.setChq_dd_no(dto.getChequeDDNo());
                       transactionDetails.setChq_dd_dte(dto.getChequeDDDate());
                       transactionDetails.setChk_dd_clear_status(0);
                       transactionDetails.setCard_no(0L);
                       transactionDetails.setCard_type("NA");
                       transactionDetails.setCard_holder_name("NA");
                       transactionDetails.setCleared_by(null);
                       transactionDetails.setChk_dd_amount(dto.getChequeDDAmount());
                       transactionDetails.setRemarks(dto.getNarration());
                       transactionDetails.setClear_entry_date(null);
                       transactionDetails.setUser_id(dto.getUserId());
                       transactionDetails.setEntry_time(LocalTime.now().toString());
                       transactionDetails.setTransaction_id(results.getId());
                       transactionDetails.setConsumer_dets_id(consumerDetailsId);

                       waterTransactionDetailsDao.save(transactionDetails);
                   }
                   else if(dto.getPaymentMode().equalsIgnoreCase("Card")) {
                       transactionDetails.setRemarks(dto.getNarration());
                       transactionDetails.setClear_entry_date(LocalDate.now()
                               .format(outPutFormat));
                       transactionDetails.setUser_id(dto.getUserId());
                       transactionDetails.setEntry_time(LocalTime.now().withNano(0).toString());
                       transactionDetails.setTransaction_id(results.getId());
                       transactionDetails.setConsumer_dets_id(consumerDetailsId);
                       transactionDetails.setCard_type(dto.getCardType());
                       transactionDetails.setCard_no(dto.getCardNo());
                       transactionDetails.setCard_holder_name(dto.getCardHolderName());
                       transactionDetails.setBank_name(dto.getBankName());
                       transactionDetails.setBranch_name(dto.getBranch());
                       transactionDetails.setChk_dd_clear_status(1);
                       waterTransactionDetailsDao.save(transactionDetails);
                   }

                   else if(dto.getPaymentMode().equalsIgnoreCase("NEFT")
                           || dto.getPaymentMode().equalsIgnoreCase("RTGS")) {

                       transactionDetails.setBank_name(dto.getBankName());
                       transactionDetails.setBranch_name(dto.getBranch());

                       if(dto.getPaymentMode().equalsIgnoreCase("NEFT")) {
                           transactionDetails.setChq_dd_no(dto.getNeftNo());
                           transactionDetails.setChq_dd_dte(dto.getNeftDate());
                       }
                       if(dto.getPaymentMode().equalsIgnoreCase("RTGS")) {
                           transactionDetails.setChq_dd_no(dto.getRtgsNo());
                           transactionDetails.setChq_dd_dte(dto.getRtgsDate());
                       }
                       transactionDetails.setChk_dd_clear_status(1);
                       transactionDetails.setCard_no(0L);
                       transactionDetails.setCard_type("NA");
                       transactionDetails.setCard_holder_name("NA");
                       transactionDetails.setCleared_by(null);
                       transactionDetails.setChk_dd_amount(dto.getChequeDDAmount());
                       transactionDetails.setRemarks(dto.getNarration());
                       transactionDetails.setClear_entry_date(null);
                       transactionDetails.setUser_id(dto.getUserId());
                       transactionDetails.setEntry_time(LocalTime.now().toString());
                       transactionDetails.setTransaction_id(results.getId());
                       transactionDetails.setConsumer_dets_id(consumerDetailsId);

                       waterTransactionDetailsDao.save(transactionDetails);
                   }

                   /*else if(!dto.getPaymentMode().equalsIgnoreCase("Cheque")
                           && !dto.getPaymentMode().equalsIgnoreCase("DD")
                   && !dto.getPaymentMode().equalsIgnoreCase("Cash")) {
                       transactionDetails.setBank_name(dto.getBankName());
                       transactionDetails.setBranch_name(dto.getBranch());
                       transactionDetails.setChq_dd_no(dto.getChequeDDNo());
                       transactionDetails.setChq_dd_dte(dto.getChequeDDDate());
                       transactionDetails.setChk_dd_clear_status(1);
                     //  if(dto.getPaymentMode().equalsIgnoreCase("Card")) {
                        //   transactionDetails.setCard_no(dto.getCardNo());
                         //  transactionDetails.setCard_type(dto.getCardType());
                          // transactionDetails.setCard_holder_name(dto.getCardHolderName());
                       //}
                       //else {
                        //   transactionDetails.setCard_no(0L);
                       //    transactionDetails.setCard_type("NA");
                         //  transactionDetails.setCard_holder_name("NA");

                       //}
                       transactionDetails.setCleared_by(null);
                       transactionDetails.setChk_dd_amount(dto.getChequeDDAmount());
                       transactionDetails.setRemarks(dto.getNarration());
                       transactionDetails.setClear_entry_date(null);
                       transactionDetails.setUser_id(dto.getUserId());
                       transactionDetails.setEntry_time(LocalTime.now().toString());
                       transactionDetails.setTransaction_id(results.getId());
                       transactionDetails.setConsumer_dets_id(consumerDetailsId);
                       waterTransactionDetailsDao.save(transactionDetails);
                   }*/

               }
               else if(consumerDetails==null) {
                   throw new BadRequestException("Consumer Not found try with another Number");
               }
           }
           catch(Exception e) {
               throw new BadRequestException(e.getMessage());
           }
        }
        else if(dto==null) {
            throw new BadRequestException("Request is null");
        }

    }

    @Override
    public List<ConsumerPaymentDetailsResponse> getConsumerPaymentDetails(String consumerNo) {
       Long consumerDetailsId=0L;
        List<ConsumerPaymentDetailsResponse> response=new ArrayList<>();
        WaterConsumerDetailsBean consumerDetailsBean = waterConsumerDetailsDao.findIdByConsumerNo(consumerNo);
        if(consumerDetailsBean !=null) {
            consumerDetailsId=consumerDetailsBean.getId();
        }
        else if(consumerDetailsBean==null) {
            throw new BadRequestException("Consumer number not Found, Try with others");
        }
        List<WaterTransactionMasterBean> transactionMasterBean = waterTransactionMasterDao.getAllTransactionByConsumerId(consumerDetailsId);
        DateTimeFormatter monthYearFormat= DateTimeFormatter.ofPattern("MMM/yyyy");
        if(transactionMasterBean.size()>0) {
            for(WaterTransactionMasterBean res : transactionMasterBean) {
                String periods=null;
                ConsumerPaymentDetailsResponse dto = new ConsumerPaymentDetailsResponse();
                List<ConsumerPaymentDetailsBean> consumerPaymentDetails=null;
               WaterTransactionDetailsBean wtrTransactionDetails=null;
                try {
                    consumerPaymentDetails = consumerPaymentDetailsDao.getConsumerPaymentDetailsByTransactionId(res.getId());
                    if(!res.getPayment_mode().equalsIgnoreCase("CASH")) {
                        wtrTransactionDetails = waterTransactionDetailsDao.findByTransactionId(res.getId());
                        if(wtrTransactionDetails!=null) {
                            dto.setChequeDate(wtrTransactionDetails.getChq_dd_dte());
                            dto.setBankName(wtrTransactionDetails.getBank_name());
                            dto.setBranchName(wtrTransactionDetails.getBranch_name());
                        }
                    }
                }
                catch(Exception e) {
                  throw new BadRequestException(e.getMessage());
                }

                if(!consumerPaymentDetails.isEmpty()) {
                    //periods=consumerPaymentDetails.get(0).getSorted_date();
                   periods=LocalDate.parse(consumerPaymentDetails.get(0).getFrom_date(),DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            .format(monthYearFormat) + "-" +
                           LocalDate.parse(consumerPaymentDetails.get(consumerPaymentDetails.size()-1).getUpto_date(),DateTimeFormatter.ofPattern("dd/MM/yyyy")).format(monthYearFormat);
                }

                dto.setPeriods(periods);
                dto.setUnit(consumerPaymentDetails.get(0).getMonthly_amount().longValue());
                dto.setReceiptNo(res.getTransaction_no());
                dto.setTransactionDate(res.getTransaction_date());
                dto.setDemand(res.getDemand_amount());
                dto.setPayableAmount(res.getPayable_amount());
                dto.setPaymentMode(res.getPayment_mode());
                dto.setPenalty(res.getPenalty());
                dto.setUserId(res.getUser_id());
                response.add(dto);
            }
        }

        return response;
    }

    @Override
    public CollectionReportDto collectionReport(String dateFrom, String dateUpto, String ward_id, String user_id, String paymentMode) {
        if(dateFrom ==null) {
          throw new BadRequestException("Date from can't be blank");
        }
        else if(dateUpto==null) {
            throw new BadRequestException("Date up to can't be blank");
        }
        DateTimeFormatter outPutFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        CollectionReportDto response =new CollectionReportDto();
        List<CollectionsBodyDto> bodyDtos=new ArrayList<>();
       // CollectionReportDto dto = new CollectionReportDto();
       // List<WaterTransactionMasterBean> finalTrnMasterResults= new ArrayList<>();
        //List<WaterTransactionDetailsBean> transactionDetails=new ArrayList<>();

        String fromDateString=LocalDate.parse(dateFrom,inputFormat).format(outPutFormat);
        String upToDateString=LocalDate.parse(dateUpto,inputFormat).format(outPutFormat);

       /* LocalDate tempDateFrom= LocalDate.parse(fromDateString,outPutFormat);
        LocalDate tempDateUpTo= LocalDate.parse(upToDateString,outPutFormat);

        Timestamp tempFrom= Timestamp.from(Instant.from(tempDateFrom)); // error here: java.time.temporal.UnsupportedTemporalTypeException: Unsupported field: InstantSeconds
        Timestamp tempToDate=Timestamp.from(Instant.from(tempDateUpTo));

        Date startDate = Date.from(tempDateFrom.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(tempDateUpTo.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<WaterTransactionMasterBean> transactionMaster=null;
        List<WaterTransactionMasterBean> paymentModeList=null;  */
        BigDecimal totalCollections=new BigDecimal(0.00);
        BigDecimal totalBounce= new BigDecimal(0.00);
        BigDecimal netCollection=new BigDecimal(0.00);
        Map<Object, Object> inputParameter=new LinkedHashMap<>();
        Long wardId = null;
        Long userId=null;
        if(ward_id.equalsIgnoreCase("All")) {
            wardId=0L;
        }
        else if(!ward_id.equalsIgnoreCase("All")) {
            wardId=Long.parseLong(ward_id);
        }
        if(user_id.equalsIgnoreCase("All")) {
            userId=-1L;
        } else if (!user_id.equalsIgnoreCase("All")) {
            userId=Long.parseLong(user_id);
        }
        if(paymentMode.equalsIgnoreCase("All")) {
            paymentMode="";
        }

        try {
            inputParameter.put(fromDateString, fromDateString);
            inputParameter.put(upToDateString, upToDateString);
            inputParameter.put(wardId, wardId);
            inputParameter.put(userId,userId);
            inputParameter.put(paymentMode,paymentMode);

            CachedRowSet crs= DBFunctionCall.simpleFunctionCallWithParameter("watermgmt.get_water_counter_collection_report(?,?,?,?,?)", 1, inputParameter);;
            while (crs.next()) {
                CollectionsBodyDto dto = new CollectionsBodyDto();
                dto.setWardNo(crs.getString(1));
                dto.setOwnerName(crs.getString(2));
                dto.setFromMonth(crs.getString(3));
                dto.setUpToMonth(crs.getString(4));
                dto.setTransactionDate(crs.getString(5));
                dto.setTransactionNo(crs.getString(6));
                dto.setModeOfPayment(crs.getString(7));
                dto.setChequeDDNo(crs.getString(8));
                dto.setBankName(crs.getString(9));
                dto.setTcName(crs.getString(10));
                dto.setPaidAmount(crs.getBigDecimal(11));
                dto.setConsumerNo(crs.getString(12));
                totalCollections = totalCollections.add(crs.getBigDecimal(11)).setScale(0, RoundingMode.CEILING);
                if(crs.getInt(14)==1) {
                    netCollection=netCollection.add(crs.getBigDecimal(11)).setScale(0, RoundingMode.CEILING);
                }
                if(crs.getInt(13)==3) {
                    totalBounce=totalBounce.add(crs.getBigDecimal(11)).setScale(0, RoundingMode.CEILING);
                }

                bodyDtos.add(dto);
            }
        }catch (Exception e) {
            log.info(e.getMessage());
        }
      /* if(!StringUtils.isBlank(dateFrom) && !StringUtils.isBlank(dateUpto) && wardId!=null && paymentMode!=null && userId!=null ) {
            transactionMaster=waterTransactionMasterDao.getCollectionReportByWardIdAndPaymentModeAndUserId(startDate,endDate,wardId,paymentMode,userId);
           paymentModeList=transactionMaster.stream().filter(v-> !v.getPayment_mode().equalsIgnoreCase("Cash")).collect(Collectors.toList());

       }
        else if(!StringUtils.isBlank(dateFrom) && !StringUtils.isBlank(dateUpto) && wardId!=null && paymentMode!=null) {
            transactionMaster=waterTransactionMasterDao.getCollectionReportByWardIdAndPaymentMode(startDate,endDate,wardId,paymentMode);
           paymentModeList=transactionMaster.stream().filter(v-> !v.getPayment_mode().equalsIgnoreCase("Cash")).collect(Collectors.toList());

       }
        else if(!StringUtils.isBlank(dateFrom) && !StringUtils.isBlank(dateUpto) && wardId!=null) {
            transactionMaster=waterTransactionMasterDao.getCollectionReportByWardId(startDate,endDate,wardId);
           paymentModeList=transactionMaster.stream().filter(v-> !v.getPayment_mode().equalsIgnoreCase("Cash")).collect(Collectors.toList());

       }
        else if(!StringUtils.isBlank(dateFrom) && !StringUtils.isBlank(dateUpto)) {
            transactionMaster = waterTransactionMasterDao
                    .getCollectionReport(startDate, endDate);
           paymentModeList=transactionMaster.stream().filter(v-> !v.getPayment_mode().equalsIgnoreCase("Cash")).collect(Collectors.toList());
        }

        //finalTrnMasterResults=transactionMaster;
//        if(wardId>0L) {
//            finalTrnMasterResults=null;
//          List<WaterTransactionMasterBean> wardTrnList=transactionMaster.stream().filter(v-> v.getWard_id()==wardId).collect(Collectors.toList());
//            finalTrnMasterResults=wardTrnList;
//        } else if (userId > 0L) {
//            finalTrnMasterResults=null;
//            List<WaterTransactionMasterBean> userIdList=transactionMaster.stream().filter(v-> v.getUser_id()==userId).collect(Collectors.toList());
//            finalTrnMasterResults=userIdList;
//        } else

           // if (paymentMode.isBlank()) {
            //finalTrnMasterResults=null;
             //paymentModeList=transactionMaster.stream().filter(v-> v.getPayment_mode().equalsIgnoreCase(paymentMode)).collect(Collectors.toList());
            //finalTrnMasterResults=paymentModeList;
            //if(paymentMode.equalsIgnoreCase("Cheque")) {
               // finalTrnMasterResults=null;
                //List<WaterTransactionMasterBean> chequeList = transactionMaster.stream().filter(v-> v.getPayment_mode().equalsIgnoreCase("Cheque")).collect(Collectors.toList());
                //finalTrnMasterResults=chequeList;
                if(!paymentModeList.isEmpty()) {
                List<Long> pickedTranIds=paymentModeList.stream().map(v->v.getId()).collect(Collectors.toList());
                for(Long trnId:pickedTranIds) {
                    //WaterTransactionDetailsBean detailsDto = new WaterTransactionDetailsBean();
                    WaterTransactionDetailsBean detailsDto = waterTransactionDetailsDao.findByTransactionId(trnId);
                    transactionDetails.add(detailsDto);
                }
                }
            //}
        //}
        if(transactionMaster.size() > 0) {
            BigDecimal totalCollections=transactionMaster.stream().map(v-> v.getPayable_amount()).reduce(BigDecimal.ZERO,BigDecimal::add);
            for(WaterTransactionMasterBean waterTransactionMasterBean: transactionMaster) {
                CollectionsBodyDto collectionReportDto = new CollectionsBodyDto();
                Optional<UserDetails> userDetails=userDetailsDao.findById(waterTransactionMasterBean.getUser_id());
                Optional<ConsumerBasicDetailsBean> consumerBasicDetailsBean = consumerBasicDetailsDao.getConsumerBasicDetailsByConsumerDetailsId(waterTransactionMasterBean.getConsumer_dets_id());
                Optional<WaterConsumerDetailsBean> consumerDetailsBean= waterConsumerDetailsDao.findById(waterTransactionMasterBean.getConsumer_dets_id());

                Optional<WardBean> wardBean = wardDao.findById(waterTransactionMasterBean.getWard_id());
                if(consumerDetailsBean.isPresent()) {
                    collectionReportDto.setConsumerNo(consumerDetailsBean.get().getConsumer_no());
                    //bodyDtos.add(collectionReportDto);
                }
                if(wardBean.isPresent()) {
                    collectionReportDto.setWardNo(wardBean.get().getWard_name());
                   // bodyDtos.add(collectionReportDto);
                }
                if(consumerBasicDetailsBean.isPresent()) {
                    //collectionReportDto.setMobileNo(consumerBasicDetailsBean.get().getMobile_no());
                    collectionReportDto.setOwnerName(consumerBasicDetailsBean.get().getName());
                    //bodyDtos.add(collectionReportDto);
                }
                if(userDetails.isPresent()) {
                    collectionReportDto.setTcName(userDetails.get().getName());
                   // bodyDtos.add(collectionReportDto);
                }
                collectionReportDto.setTransactionNo(waterTransactionMasterBean.getTransaction_no());
                collectionReportDto.setTransactionDate(waterTransactionMasterBean.getTransaction_date());
                collectionReportDto.setFromMonth(String.valueOf(LocalDate.parse(dateFrom).getMonth()));
                collectionReportDto.setPaidAmount(waterTransactionMasterBean.getDemand_amount());
                //collectionReportDto.setFromYear(String.valueOf(LocalDate.parse(dateFrom).getYear()));
                collectionReportDto.setUpToMonth(String.valueOf(LocalDate.parse(dateUpto).getMonth()));
                collectionReportDto.setModeOfPayment(waterTransactionMasterBean.getPayment_mode());
               //
                if(transactionDetails.size()>0) {
                    for(WaterTransactionDetailsBean waterDetails : transactionDetails) {
                       // CollectionsBodyDto collectionReportDto = new CollectionsBodyDto();
                        collectionReportDto.setBankName(waterDetails.getBank_name());
                        if(paymentMode.equalsIgnoreCase("Cheque") || paymentMode.equalsIgnoreCase("DD")) {
                            collectionReportDto.setChequeDDNo(waterDetails.getChq_dd_no());
                        }

                    }
                }
                bodyDtos.add(collectionReportDto);
            }
            response.setCollectionsBody(bodyDtos);
            response.setTotalCollection(totalCollections);
            response.setNetCollection(totalCollections);

        }
*/
        response.setCollectionsBody(bodyDtos);
        response.setTotalCollection(totalCollections);
        response.setNetCollection(netCollection);
        response.setTotalBounce(totalBounce);
        return response;
    }

	
	/*
	 * @Value("${aws.accessKeyId}") private String accessKey;
	 * 
	 * @Value("${aws.secretKey}") private String secretKey;
	 * 
	 * @Value("${aws.region}") private String region;
	 * 
	 * @Value("${aws.water.isNagarNigamEmployee.file.suffix}") private String
	 * fileSuffix;
	 * 
	 * @Value("${aws.water.isNagarNigamEmployee.bucketName}") private String
	 * bucketName;
	 */
	  
	 //// @Override public void IsNagarNigamEmployeeDocumentUpload(MultipartFile
	  ////file,String consumerNo) throws IOException {
	 //// UploadFile.AWS_Upload(accessKey,secretKey,region,bucketName,consumerNo,file);
	  
	///  }
	 

}
