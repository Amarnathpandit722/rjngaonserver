package gov.municipal.suda.modules.wastemgmt.service.master;


import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.modules.property.dao.master.BankNameDao;
import gov.municipal.suda.modules.property.model.master.BankNameBean;
import gov.municipal.suda.modules.wastemgmt.dao.master.*;
import gov.municipal.suda.modules.wastemgmt.dao.transaction.*;
import gov.municipal.suda.modules.wastemgmt.dto.*;
import gov.municipal.suda.modules.wastemgmt.model.master.*;
import gov.municipal.suda.modules.wastemgmt.model.transaction.*;
import gov.municipal.suda.modules.wastemgmt.repos.UserChargeCustomConsumerDetailsSearch;
import gov.municipal.suda.modules.wastemgmt.repos.UserChargeCustomConsumerMasterSearch;
import gov.municipal.suda.modules.wastemgmt.service.transaction.ConsumerCollectionService;
import gov.municipal.suda.modules.wastemgmt.service.transaction.LastPaymentUpdateService;
import gov.municipal.suda.usermanagement.dao.UserDetailsDao;
import gov.municipal.suda.usermanagement.model.UserDetails;
import gov.municipal.suda.util.Generate;
import gov.municipal.suda.util.common.LastPaymentUpdateMonthDropDownDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ConsumerMasterServiceImpl implements ConsumerMasterService {
    @Autowired
    EntityManager entityManager;
    @Autowired
    ConsumerCategoryDao consumerCategoryDao;
    @Autowired
    ConsumerRangeDao consumerRangeDao;
    @Autowired
    ConsumerRateChartDao consumerRateChartDao;
    @Autowired
    ConsumerMasterDao consumerMasterDao;
    @Autowired
    ConsumerDetailService consumerDetailService;
    @Autowired
    ConsumerDetailsDao consumerDetailsDao;
    @Autowired
    ConsumerRateDetailsDao consumerRateDetailsDao;
    @Autowired
    ConsumerTransactionDao consumerPaymentTrnDao;
    @Autowired
    ConsumerTransactionDetailDao consumerTransactionDetailDao;
    @Autowired
    ConsumerDemandDao consumerDemandDao;
    @Autowired
    ConsumerCollectionService collectionService;
    @Autowired
    ConsumerLastPaymentRecordDao lastPaymentRecordDao;
    @Autowired
    ConsumerCollectionDao consumerCollectionDao;
    @Autowired
    BankNameDao bankNameDao;
    @Autowired
    UserDetailsDao userDetailsDao;
    @Autowired
    LastPaymentUpdateService lastPaymentUpdateService;

    @Autowired
    UserChargeCustomConsumerMasterSearch customConsumerMaster;

    @Autowired
    UserChargeCustomConsumerDetailsSearch customConsumerDetails;

    @Override
    public List<ConsumerRangeMasterBean> fetchConsumerRange() {
        return consumerRangeDao.findAll();
    }

    @Override
    public List<ConsumerCategoryMasterBean> fetchAllConsumerCategory() {
        return consumerCategoryDao.findAll();
    }

    @Override
    public List<ConsumerViewDTO> fetchAllConsumer(String ward_id, String holding_no, String consumer_no, String consumer_name,Long mobile_no) {
        List<ConsumerViewDTO> consumerViewDTOS = new ArrayList<>();
        List<ConsumerMasterBean> consumerMasterList = new ArrayList<>();
        List<ConsumerDetailsBean> consumerDetailsList = new ArrayList<>();

        if(!ward_id.isEmpty() || !holding_no.isEmpty()) {
            consumerMasterList=customConsumerMaster.fetchCustomConsumerMasterSearch(ward_id,holding_no);
            if(!consumerMasterList.isEmpty()) {
                for(ConsumerMasterBean consumerMaster: consumerMasterList){
                    Optional<ConsumerDetailsBean> consumerDetails = consumerDetailsDao.findConsumerDetailsByConsumerMasterId(consumerMaster.getId());
                    ConsumerViewDTO dto = new ConsumerViewDTO();
                    dto.setWard_no(consumerMaster.getWard_no());
                    dto.setHolding_no(consumerMaster.getHolding_no());
                    dto.setConsumer_master_id(consumerMaster.getId());
                    if(consumerDetails.isPresent()) {
                        dto.setConsumer_no(consumerDetails.get().getConsumer_no());
                        dto.setConsumer_details_id(consumerDetails.get().getId());
                        dto.setConsumer_name(consumerDetails.get().getConsumer_name());
                        dto.setConsumer_type(consumerDetails.get().getConsumer_type());
                        dto.setConsumer_no(consumerDetails.get().getConsumer_no());
                        dto.setMobile_no(consumerDetails.get().getMobile_no());
                    }
                    consumerViewDTOS.add(dto);
                }
            }
        }

        if(!consumer_no.isEmpty() || !consumer_name.isEmpty()|| mobile_no!=null) {
            consumerDetailsList=customConsumerDetails.fetchCustomConsumerDetailsSearch(consumer_no,consumer_name,mobile_no);
            if(!consumerDetailsList.isEmpty()) {
                for(ConsumerDetailsBean consumerDetails: consumerDetailsList) {
                    ConsumerViewDTO dto = new ConsumerViewDTO();
                    Optional<ConsumerMasterBean> consumerMaster = consumerMasterDao.findById(consumerDetails.getConsumer_mstr_id());
                    if(consumerMaster.isPresent()) {
                        dto.setWard_no(consumerMaster.get().getWard_no());
                        dto.setHolding_no(consumerMaster.get().getHolding_no());
                        dto.setConsumer_master_id(consumerMaster.get().getId());
                    }
                    dto.setConsumer_no(consumerDetails.getConsumer_no());
                    dto.setConsumer_details_id(consumerDetails.getId());
                    dto.setConsumer_name(consumerDetails.getConsumer_name());
                    dto.setConsumer_type(consumerDetails.getConsumer_type());
                    dto.setConsumer_no(consumerDetails.getConsumer_no());
                    dto.setMobile_no(consumerDetails.getMobile_no());

                    consumerViewDTOS.add(dto);
                }
            }
        }

        if(!consumerMasterList.isEmpty() && !consumerDetailsList.isEmpty()){
            consumerViewDTOS.clear();
            Set<Long> consumerIdsFromMaster = new HashSet<>();
            Set<Long> consumerIdsFromConsumerDetails= new HashSet<>();
            for(ConsumerMasterBean consumerMasterBean: consumerMasterList){
                consumerIdsFromMaster.add(consumerMasterBean.getId());
            }
            for(ConsumerDetailsBean consumerDetailsBean: consumerDetailsList){
                consumerIdsFromConsumerDetails.add(consumerDetailsBean.getConsumer_mstr_id());
            }
            consumerIdsFromMaster.retainAll(consumerIdsFromConsumerDetails);

            for(Long consumerMasterIds : consumerIdsFromMaster) {
                ConsumerViewDTO dto = new ConsumerViewDTO();
                Optional<ConsumerMasterBean> consumerMaster = consumerMasterDao.findById(consumerMasterIds);
                Optional<ConsumerDetailsBean> consumerDetails = consumerDetailsDao.findConsumerDetailsByConsumerMasterId(consumerMasterIds);
                if (consumerMaster.isPresent()) {
                    dto.setWard_no(consumerMaster.get().getWard_no());
                    dto.setHolding_no(consumerMaster.get().getHolding_no());
                    dto.setConsumer_master_id(consumerMaster.get().getId());
                }
                if (consumerDetails.isPresent()) {
                    dto.setConsumer_no(consumerDetails.get().getConsumer_no());
                    dto.setConsumer_details_id(consumerDetails.get().getId());
                    dto.setConsumer_name(consumerDetails.get().getConsumer_name());
                    dto.setConsumer_type(consumerDetails.get().getConsumer_type());
                    dto.setConsumer_no(consumerDetails.get().getConsumer_no());
                    dto.setMobile_no(consumerDetails.get().getMobile_no());
                }
                consumerViewDTOS.add(dto);

            }
        }
        return consumerViewDTOS;
    }

    @Override
    public Optional<ConsumerRateChartBean> fetchMonthlyAmount(String consumerRangeMstrId) {
//        String fromDate=effectYear.substring(0,4)+"-"+effectMonth+"-"+"01";
//        String toDate=effectYear.substring(0,4)+"-"+effectMonth+"-"+"31";
//        Timestamp fee_effectdate = Timestamp.valueOf(fromDate + " 00:00:00.00");
//        Timestamp fee_effectdate1 = Timestamp.valueOf(toDate + " 23:59:59.999999999");
        List<ConsumerRateChartBean> monthly_amount = consumerRateChartDao.fetchMonthlyAmount(Long.valueOf(consumerRangeMstrId));
        Optional<ConsumerRateChartBean> latestObject = monthly_amount.stream()
                .sorted(Comparator.comparing(ConsumerRateChartBean::getAmount).reversed()) // sort by stampdate in descending order
                .findFirst();
        return latestObject;
    }

    @Override
    @Transactional(rollbackOn = BadRequestException.class)
    public String createConsumer(ConsumerEntryDTO consumerEntryDTO) throws SQLException {
        ConsumerMasterBean consumer_entry = new ConsumerMasterBean();
        //String consumer_no= ThreadLocalRandom.current().nextLong(10000L, 100000L)+""+consumerEntryDTO.getWard_id();
        String consumer_no = Generate.generateConsumerNo();
        consumer_entry.setHolding_no(consumerEntryDTO.getHolding_no());
        consumer_entry.setWard_id(String.valueOf(consumerEntryDTO.getWard_id()));
        consumer_entry.setWard_no(consumerEntryDTO.getWard_no());
        consumer_entry.setPolice_station(consumerEntryDTO.getPolice_station());
        consumer_entry.setAddress(consumerEntryDTO.getAddress());
        consumer_entry.setLand_mark(consumerEntryDTO.getLand_mark());
        consumer_entry.setCreated_byid(consumerEntryDTO.getCreated_byid());
        consumer_entry.setStatus(1);
        consumer_entry.setStampdate(Timestamp.valueOf(LocalDateTime.now()));
        try {
            ConsumerMasterBean entryView = consumerMasterDao.save(consumer_entry);
            consumerDetailService.addConsumerDetails(entryView.getId(), consumerEntryDTO, consumer_no);
        } catch (BadRequestException e) {

            return new BadRequestException("Data not save ").toString();
        }
        return consumer_no;
    }

    @Override
        public List<ConsumerDetailsViewDTO> fetchConsumerByConsumerNo(String consumerNo) {
        List<ConsumerDetailsViewDTO> consumerDetailsViewDTOS = new ArrayList<>();
        ConsumerDetailsViewDTO consumerDetailsViewDTO = new ConsumerDetailsViewDTO();
        List<LastPaymentUpdateMonthDropDownDto> dropDown= lastPaymentUpdateService.LastPaymentUpdateMonthDropDown(consumerNo);
        Optional<ConsumerDetailsBean> consumerDetail = consumerDetailsDao.findByMstrIdConsumNo(consumerNo);
        Optional<ConsumerMasterBean> consumer = consumerMasterDao.findById(consumerDetail.get().getConsumer_mstr_id());
        consumerDetailsViewDTO.setHolding_no(consumer.get().getHolding_no());
        consumerDetailsViewDTO.setAddress(consumer.get().getAddress());
        consumerDetailsViewDTO.setLand_mark(consumer.get().getLand_mark());
        consumerDetailsViewDTO.setPolice_station(consumer.get().getPolice_station());
        consumerDetailsViewDTO.setWard_id(consumer.get().getWard_id());
        consumerDetailsViewDTO.setWard_no(consumer.get().getWard_no());
        consumerDetailsViewDTO.setConsumer_mstr_id(consumer.get().getId());
        consumerDetailsViewDTO.setConsumer_no(consumerDetail.get().getConsumer_no());
        consumerDetailsViewDTO.setConsumer_name(consumerDetail.get().getConsumer_name());
        consumerDetailsViewDTO.setGradian_name(consumerDetail.get().getGradian_name());
        consumerDetailsViewDTO.setRelation(consumerDetail.get().getRelation());
        consumerDetailsViewDTO.setMobile_no(consumerDetail.get().getMobile_no());
        consumerDetailsViewDTO.setConsumer_type(consumerDetail.get().getConsumer_type());
        consumerDetailsViewDTO.setHouse_flat_no(consumerDetail.get().getHouse_flat_no());
        consumerDetailsViewDTO.setDropDown(dropDown);
        consumerDetailsViewDTOS.add(consumerDetailsViewDTO);
        return consumerDetailsViewDTOS;
    }

    @Override
    public List<AreaDetailsViewDTO> fetchAreaDetailByConsumerNo(String consumerNo) {
        List<AreaDetailsViewDTO> areaDetailsViewDTOS = new ArrayList<>();
        Long consumerMasterId = consumerDetailsDao.findConsumerMasterIdByConsumerNo(consumerNo);
        List<ConsumerDemandBean> demandBean;
        Set<Long> rateChartIds=null;
        if(consumerMasterId!=null) {
            Long consumerDetailsId = consumerDetailsDao.findConsumerDetailsByConsumerMasterId(consumerMasterId).orElse(null).getId();
            if(consumerDetailsId!=null) {
                 demandBean = consumerDemandDao.fetchGeneratedDemandByConsumerDetailsId(consumerDetailsId);
                 if(demandBean!=null) {
                     rateChartIds=demandBean.stream().map(v-> v.getRate_chart_id()).collect(Collectors.toSet());
                 }
            }
        log.info("consumer detail id.............." + consumerMasterId);

                //ConsumerRateChartBean rateBean=null;
                if(rateChartIds.size()>0) {
                    for (Long rateChartId : rateChartIds) {
                        ConsumerRateChartBean rateBean = consumerRateChartDao.findById(rateChartId).orElse(null);
                        if (rateBean != null) {
                            AreaDetailsViewDTO areaDetailsViewDTO = new AreaDetailsViewDTO();
                            ConsumerRateDetailsBean rate = consumerRateDetailsDao.findConsumerRangeDtlsByDetailsIdAndRangeMasterId(consumerMasterId, rateBean.getConsumer_range_mstr_id());
                            String consumer_category = consumerCategoryDao.findById(rate.getConsumer_cat_mstr_id()).get().getCategory_name();
                            String consumer_range = consumerRangeDao.findById(rate.getConsumer_range_mstr_id()).get().getRange_name();
                            areaDetailsViewDTO.setDoe(rateBean.getFee_effectdate().substring(0, 10));
                            areaDetailsViewDTO.setAmount(rateBean.getAmount());
                            areaDetailsViewDTO.setConsumerRateId(rate.getId());
                            areaDetailsViewDTO.setNoof_sqft_truck_room(rate.getNoof_sqft_truck_room());
                            areaDetailsViewDTO.setConsumer_category(consumer_category);
                            areaDetailsViewDTO.setConsumer_range(consumer_range);
                            areaDetailsViewDTO.setOld_rate_chart_id(rateBean.getId());
                            areaDetailsViewDTOS.add(areaDetailsViewDTO);
                        }
                    }
                }
        }

        else {
            throw new BadRequestException("Consumer number not found");
        }
       return areaDetailsViewDTOS;
    }

    @Override
    public List<MonthlyRateViewDTO> fetchMonthlyRateByConsumerNo(String consumerNo) {
        List<MonthlyRateViewDTO> monthlyRateViewDTOS = new ArrayList<>();
        Long consumer_master_id = consumerDetailsDao.findConsumerMasterIdByConsumerNo(consumerNo);
        if(consumer_master_id!=null) {
            List<ConsumerDemandBean> demandBean;
            Set<Long> rateChartIds = null;
            Long consumerDetailsId = consumerDetailsDao.findConsumerDetailsByConsumerMasterId(consumer_master_id).orElse(null).getId();
            if (consumerDetailsId != null) {
                demandBean = consumerDemandDao.fetchGeneratedDemandByConsumerDetailsId(consumerDetailsId);
                if (demandBean != null) {
                    rateChartIds = demandBean.stream().map(v -> v.getRate_chart_id()).collect(Collectors.toSet());
                }
            }
            if (rateChartIds.size() > 0) {
                for (Long rateChartId: rateChartIds) {
                    MonthlyRateViewDTO monthlyRateViewDTO = new MonthlyRateViewDTO();
                    ConsumerRateChartBean rateChartBean = consumerRateChartDao.findById(rateChartId).orElse(null);
                    if (rateChartBean != null) {

                        monthlyRateViewDTO.setDoe(rateChartBean.getFee_effectdate().substring(0,10));
                        monthlyRateViewDTO.setMonthly_rate(rateChartBean.getAmount());
                        monthlyRateViewDTOS.add(monthlyRateViewDTO);
                    }
                }
            }

        }

        else {
            throw new BadRequestException("Consumer Number not found");
        }
        return monthlyRateViewDTOS;
    }

    @Override
    public List<ConsumerDueDTO> fetchDeuDetailsByConsumerNo(String consumerNo) {
        Long consumer_details_id = consumerDetailsDao.findConsumerDetailsIdByConsumerNo(consumerNo);
        List<ConsumerDueDTO> receipt = new ArrayList<>();
        List<ConsumerDemandBean> dueDemandList=consumerDemandDao.fetchConsumerDueDemandByDtlId(consumer_details_id);
        if(!dueDemandList.isEmpty()) {
            for (ConsumerDemandBean dueDemand : dueDemandList) {
                ConsumerDueDTO dto = new ConsumerDueDTO();
                dto.setDemand_id(dueDemand.getId());
                dto.setDemand_amount(dueDemand.getDemand_amount());
                dto.setDemand_from(dueDemand.getDemand_from());
                dto.setDemand_to(dueDemand.getDemand_to());
                receipt.add(dto);
            }
        }
        /*String jpql = "select COALESCE(sum(a.demand_amount),0)as demand_amount \n" +
                "FROM wastemgmt.tbl_consumer_demand a where a.consumer_detail_id='" + consumer_details_id + "' and a.payment_status=0";
        Query query = entityManager.createNativeQuery(jpql);
        BigDecimal demandAmount = (BigDecimal) query.getSingleResult();

        String jpql1 = "select a.demand_from \n" +
                "FROM wastemgmt.tbl_consumer_demand a where a.consumer_detail_id='" + consumer_details_id + "' and a.payment_status=0 order by id limit 1";
        Query query1 = entityManager.createNativeQuery(jpql1);
        String demand_from=null;
        if(query1.getResultList().size()>0){
            demand_from = (String) query1.getSingleResult();
        }
        else{
            demand_from ="NA";
        }
        String jpql2 = "select a.demand_to \n" +
                "FROM wastemgmt.tbl_consumer_demand a where a.consumer_detail_id='" + consumer_details_id + "' and a.payment_status=0 order by id desc limit 1";
        Query query2 = entityManager.createNativeQuery(jpql2);
        String demand_to=null;
        if(query2.getResultList().size()>0){
            demand_to = (String) query2.getSingleResult();
        }
        else {
            demand_to ="NA";
        }

        List<BigDecimal> results = query.getResultList();
        List<ConsumerDueDTO> receipt = new ArrayList<>();
        ConsumerDueDTO consumerDueDTO = new ConsumerDueDTO();
        if(demand_from.equals("NA") && demand_to.equals("NA") && demandAmount.compareTo(BigDecimal.ZERO) == 0){
            receipt=new ArrayList<>();
        }
        else{
        consumerDueDTO.setDemand_amount(demandAmount);
        consumerDueDTO.setDemand_from(demand_from);
        consumerDueDTO.setDemand_to(demand_to);
        receipt.add(consumerDueDTO);
        }*/
        return receipt;
    }

    @Override
    public List<PaymentReceiptViewDTO> getPaymentReceiptByTrnNo(String transactionNo) {
        List<PaymentReceiptViewDTO> receipt = new ArrayList<>();
        PaymentReceiptViewDTO response = new PaymentReceiptViewDTO();
        List<ConsumerTransactionBean> consumerTransactionBeans = consumerPaymentTrnDao.fetchConsumerTrnByNo(transactionNo);
        Optional<ConsumerDetailsBean> consumerDetail = consumerDetailsDao.findById(consumerTransactionBeans.get(0).getConsumer_detail_id());
        Optional<ConsumerMasterBean> consumerMaster = consumerMasterDao.findById(consumerDetail.get().getConsumer_mstr_id());
        Optional<ConsumerTransactionDetailBean> consumerTransactionDetails = consumerTransactionDetailDao
                .fetchConsumerTrnDetailByMstrId(consumerTransactionBeans.get(0).getId()); // it may have empty response in case of CASH transaction.
        //List<ConsumerLastPaymentBean> lastPaymentRecord = lastPaymentRecordDao.fetchLastPaymentRecord(consumerTransactionBeans.get(0).getConsumer_detail_id());
        //Optional<ConsumerLastPaymentBean> latestObject = lastPaymentRecord.stream()
         //       .sorted(Comparator.comparing(ConsumerLastPaymentBean::getStampdate).reversed()) // sort by stampdate in descending order
         //       .findFirst();
        //Date Formatter
        DateTimeFormatter monthYearFormat= DateTimeFormatter.ofPattern("MMM-yyyy");
        List<ConsumerDemandBean> demandList= consumerDemandDao.fetchGeneratedDemandByConsumerDetailsId(consumerDetail.get().getId())
                .stream().sorted(Comparator.comparing(ConsumerDemandBean::getDemand_from)).collect(Collectors.toList());
        String periods=LocalDate.parse(demandList.get(0).getDemand_from(),DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .format(monthYearFormat) +"-"+LocalDate.parse(demandList.get(demandList.size()-1).getDemand_to()
                ,DateTimeFormatter.ofPattern("yyyy-MM-dd")).format(monthYearFormat);
        //String startedFrom=demandList.get(0).getDemand_from();
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
       // log.info("Start date {} ",LocalDate.parse(startedFrom,DateTimeFormatter.ofPattern("yyyy-MM-dd")).format(monthYearFormat));
        response.setTransaction_no(consumerTransactionBeans.get(0).getTransaction_no());
        response.setTransaction_date(LocalDate.parse(consumerTransactionBeans.get(0).getTransaction_date()).format(outputFormat));
        response.setConsumer_no(consumerDetail.get().getConsumer_no());
        response.setConsumer_name(consumerDetail.get().getConsumer_name());
        response.setGuardian_name(consumerDetail.get().getGradian_name());
        response.setMobile_no(consumerDetail.get().getMobile_no());
        response.setWard_no(consumerMaster.get().getWard_no());
        response.setHolding_no(consumerMaster.get().getHolding_no());
        response.setPayable_amount(consumerTransactionBeans.get(0).getPayable_amt());
        response.setPayment_mode(consumerTransactionBeans.get(0).getPayment_mode());
        response.setAccount_description("Solid Waste User Charge & Others"); // need to verify and ask
        response.setDepartment_section("Revenue Section");
        response.setAddress(consumerMaster.get().getAddress());
        response.setPeriods(periods);
//        if (latestObject.isPresent()) {
//            String monthName1 = Month.of(Integer.parseInt(latestObject.get().getFrm_month().substring(5,7))).name();
//            response.setFrom_yr_mnth(monthName1+"-"+latestObject.get().getFrm_month().substring(0,4));
//            String monthName2 = Month.of(Integer.parseInt(latestObject.get().getUpto_month().substring(0,2))).name();
//            response.setTo_yr_mnth(monthName2+"-"+latestObject.get().getUpto_month().substring(3,7));
//        }
//        response.setFrom_yr_mnth("April"+"-"+consumerTransactionBeans.get(0).getTransaction_date().substring(0,4));
//        int to_month=Integer.parseInt(consumerTransactionBeans.get(0).getTransaction_date().substring(0,4))+1;
//        response.setTo_yr_mnth("March"+"-"+to_month);
        if (consumerTransactionDetails.isPresent()) {
            response.setCheque_no(consumerTransactionDetails.get().getCheque_no());
            response.setCheque_date(consumerTransactionDetails.get().getCheque_date());
            response.setBank_name(consumerTransactionDetails.get().getBank_name());
            response.setBranch_name(consumerTransactionDetails.get().getBranch_name());
        }
        receipt.add(response);
        return receipt;
    }

    @Override
    public List<ConsumerDemandViewDTO> getDemandByConsumerNo(String consumerNo) {
        Long consumer_details_id = consumerDetailsDao.findConsumerDetailsIdByConsumerNo(consumerNo);
        List<ConsumerDemandViewDTO> consumerDemandViewDTOS = new ArrayList<>();
        List<ConsumerDemandBean> consumerDemandBeans = consumerDemandDao.fetchConsumerDueDemandByDtlId(consumer_details_id);
        for (ConsumerDemandBean consumer : consumerDemandBeans) {
            ConsumerDemandViewDTO consumerDemandViewDTO = new ConsumerDemandViewDTO();
            consumerDemandViewDTO.setId(consumer.getId());
            consumerDemandViewDTO.setDemand_from(consumer.getDemand_from());
            consumerDemandViewDTO.setDemand_to(consumer.getDemand_to());
            consumerDemandViewDTO.setDemand_amount(consumer.getDemand_amount());
            consumerDemandViewDTOS.add(consumerDemandViewDTO);
        }
        return consumerDemandViewDTOS;
    }

    @Override
    public List<PaymentViewDTO> fetchPaymentDetailsByConsumerNo(String consumerNo) {
        Long consumer_details_id = consumerDetailsDao.findConsumerDetailsIdByConsumerNo(consumerNo);
        List<ConsumerTransactionBean> consumerTransactionBeans = consumerPaymentTrnDao.fetchTrnByDtlId(consumer_details_id);
        List<PaymentViewDTO> paymentViewDTOS = new ArrayList<>();
        for (ConsumerTransactionBean consumerTrn : consumerTransactionBeans) {
            PaymentViewDTO paymentViewDTO = new PaymentViewDTO();
            paymentViewDTO.setTransaction_no(consumerTrn.getTransaction_no());
            paymentViewDTO.setPayment_mode(consumerTrn.getPayment_mode());
            paymentViewDTO.setTot_amount(consumerTrn.getPayable_amt());
            paymentViewDTO.setFrom_month("April"+"-"+consumerTrn.getTransaction_date().substring(0,4));
            int to_month=Integer.parseInt(consumerTrn.getTransaction_date().substring(0,4))+1;
            paymentViewDTO.setTo_month("March"+"-"+to_month);
            Optional<UserDetails> userDetails= userDetailsDao.findById(Long.valueOf(consumerTrn.getUser_id()));
            if(userDetails.isPresent()) {
                paymentViewDTO.setCollected_by(userDetails.get().getName());
            }
            paymentViewDTO.setTransaction_date(consumerTrn.getTransaction_date());
            paymentViewDTOS.add(paymentViewDTO);
        }
        return paymentViewDTOS;
    }

    @Override
    @Transactional(rollbackOn = BadRequestException.class)
    public String consumerPayment(ConsumerPaymentReqDTO consumerPaymentReqDTO) {

        String generateTransactionNo = null;
        try {
            String finalRemove = null;
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
            String removeDash = timestamp.toString().replaceAll("\\-", "");
            String colonSeperation = removeDash.replaceAll("\\:", "");
            String splitString = colonSeperation.split("\\.", 2)[0];
            finalRemove = splitString.replaceAll("\\s+", "");
            log.info("Final Remove ", finalRemove);
            Long getMaxValue = consumerPaymentTrnDao.generateId();
            Long consumer_details_id = consumerDetailsDao.findConsumerDetailsIdByConsumerNo(consumerPaymentReqDTO.getConsumer_no());
            String ward_id = consumerMasterDao.findById(Long.valueOf(consumerPaymentReqDTO.getConsumer_mstr_id())).isPresent() ? consumerMasterDao.findById(Long.valueOf(consumerPaymentReqDTO.getConsumer_mstr_id())).get().getWard_id() : null;

            BigDecimal total_amount = new BigDecimal(0.00);
            if(consumerPaymentReqDTO.getId() !=null) {
                List<ConsumerDemandBean> dueDemand=consumerDemandDao.fetchConsumerDueDemandByDtlId(consumer_details_id);
                    if(!dueDemand.isEmpty()) {
                        total_amount = dueDemand.stream().map(v -> v.getDemand_amount()).reduce(BigDecimal::add).orElse(new BigDecimal(0.00));
                    }
                ConsumerTransactionBean cashTransaction = new ConsumerTransactionBean();
                Integer nextId = getMaxValue.intValue();//cashTransactionDao.generateId(); // need to ask
                generateTransactionNo = nextId.toString().concat(finalRemove);
                log.info("Current Transaction No{} ", generateTransactionNo);
                if(nextId > 0 && finalRemove !=null) {
                    cashTransaction.setConsumer_detail_id(consumer_details_id);
                    cashTransaction.setWard_id(Long.valueOf(ward_id));
                    cashTransaction.setTransaction_no(generateTransactionNo);
                    cashTransaction.setTransaction_date(LocalDateTime.now().toLocalDate().toString()); // need to ask to reform
                    cashTransaction.setPayable_amt(total_amount.setScale(2, RoundingMode.CEILING)); // what is the difference between payable and total demand amount, need to ask
                    cashTransaction.setPenalty(BigDecimal.valueOf(0.00)); // get penality from demand table
                    cashTransaction.setDiscount(BigDecimal.valueOf(0.00));
                    cashTransaction.setDemand_amt(total_amount.setScale(2, RoundingMode.CEILING));
                    cashTransaction.setPayment_mode(consumerPaymentReqDTO.getPayment_mode());
                    cashTransaction.setRemarks(consumerPaymentReqDTO.getNarration());
                    if(consumerPaymentReqDTO.getPayment_mode().equalsIgnoreCase("Cheque")
                            || consumerPaymentReqDTO.getPayment_mode().equalsIgnoreCase("DD")) {
                        cashTransaction.setCash_verify_id(consumerPaymentReqDTO.getUser_id());
                        cashTransaction.setCash_verify_status(1L);
                        cashTransaction.setCancel_status(1L);
                        cashTransaction.setCash_verify_date(LocalDate.now().toString());

                    }
                    else if(consumerPaymentReqDTO.getPayment_mode().equalsIgnoreCase("Cash")) {
                        cashTransaction.setCash_verify_id(null);
                        cashTransaction.setCash_verify_status(0L);
                        cashTransaction.setCancel_status(0L);
                        cashTransaction.setCash_verify_date(null);
                    }
                    else {
                        cashTransaction.setCash_verify_id(consumerPaymentReqDTO.getUser_id());
                        cashTransaction.setCash_verify_status(1L);
                        cashTransaction.setCancel_status(0L);
                        cashTransaction.setCash_verify_date(LocalDate.now().toString());
                    }


                    cashTransaction.setIp_address(consumerPaymentReqDTO.getIp_address());


                    cashTransaction.setStampdate(Timestamp.valueOf(LocalDateTime.now()));
                    cashTransaction.setUser_id(Math.toIntExact(consumerPaymentReqDTO.getUser_id()));
                    cashTransaction.setStatus(1);
                    ConsumerTransactionBean returnResult = consumerPaymentTrnDao.save(cashTransaction);
                    if(returnResult !=null) {
                        for (Long id : consumerPaymentReqDTO.getId()) {
                            Optional<ConsumerDemandBean> findActiveDemandResult = consumerDemandDao.findById(id);
                            ConsumerDemandBean findActiveDemand = findActiveDemandResult.isPresent() ? findActiveDemandResult.get() : null;

                            if (findActiveDemand != null) {

                                CollectionMasterBean collectionMasterBean = new CollectionMasterBean();
                                collectionMasterBean.setWard_id(Long.valueOf(ward_id));
                                collectionMasterBean.setConsumer_detail_id(consumer_details_id);
                                collectionMasterBean.setDemand_details_id(id);
                                collectionMasterBean.setTransaction_mstr_id(returnResult.getId());
                                collectionMasterBean.setTotal_amt(findActiveDemand.getDemand_amount());
                                collectionMasterBean.setFy_id(findActiveDemand.getFinancial_year_id());
                                collectionMasterBean.setUser_id(consumerPaymentReqDTO.getUser_id());
                                collectionMasterBean.setStampdate(Timestamp.valueOf(LocalDateTime.now()));
                                collectionMasterBean.setMonth(Integer.valueOf(findActiveDemand.getDemand_from().substring(5, 7)));
                                collectionMasterBean.setYear(findActiveDemand.getDemand_from().substring(0, 4));
                                collectionMasterBean.setOld_ward_id(Long.valueOf(ward_id));
                                collectionMasterBean.setStatus(1);
                                CollectionMasterBean collectionBeanResult= consumerCollectionDao.save(collectionMasterBean);
                                if(collectionBeanResult == null) {
                                    throw new BadRequestException("Record not saved in Collection Table");
                                }
                                findActiveDemand.setPayment_status(1L);
                                consumerDemandDao.save(findActiveDemand); // set paid status

                            } else if (findActiveDemand == null) {
                                // throw new BadRequestException("No Active demand");
                                return "No Active Demand";

                            }
                        }

                    }
                    else if(returnResult==null) {
                        throw new BadRequestException("Record not saved into the Transaction Master Table");
                    }

                    if (consumerPaymentReqDTO.getPayment_mode().equals("CHEQUE") || consumerPaymentReqDTO.getPayment_mode().equals("Cheque")) {
                        ConsumerTransactionDetailBean chequeDDCardTransactionBean = new ConsumerTransactionDetailBean();
                        chequeDDCardTransactionBean.setTranscation_mstr_id(returnResult.getId());
                        chequeDDCardTransactionBean.setConsumer_detail_id(consumer_details_id);
                        chequeDDCardTransactionBean.setCheque_no(consumerPaymentReqDTO.getCheque_no());
                        chequeDDCardTransactionBean.setCheque_date(consumerPaymentReqDTO.getCheque_date());
                        chequeDDCardTransactionBean.setAmount(total_amount.setScale(2, RoundingMode.CEILING));
                        chequeDDCardTransactionBean.setCheque_status(0);
                        if (consumerPaymentReqDTO.getBank_name().equals("OTHERS")) {
                            chequeDDCardTransactionBean.setBank_name(consumerPaymentReqDTO.getOthers_bank_name());
                            BankNameBean bankNameBean = new BankNameBean();
                            bankNameBean.setBank_name(consumerPaymentReqDTO.getOthers_bank_name());
                            bankNameDao.save(bankNameBean);
                        } else {
                            chequeDDCardTransactionBean.setBank_name(consumerPaymentReqDTO.getBank_name());
                            chequeDDCardTransactionBean.setBranch_name(consumerPaymentReqDTO.getBranch());
                        }
                        chequeDDCardTransactionBean.setBranch_name(consumerPaymentReqDTO.getBranch());
                        chequeDDCardTransactionBean.setAmount(total_amount.setScale(2, RoundingMode.CEILING));
                        consumerTransactionDetailDao.save(chequeDDCardTransactionBean);
                    }
                    if (consumerPaymentReqDTO.getPayment_mode().equals("CARD") || consumerPaymentReqDTO.getPayment_mode().equals("Card")) {
                        ConsumerTransactionDetailBean chequeDDCardTransactionBean = new ConsumerTransactionDetailBean();
                        chequeDDCardTransactionBean.setTranscation_mstr_id(returnResult.getId());
                        chequeDDCardTransactionBean.setConsumer_detail_id(consumer_details_id);

                        chequeDDCardTransactionBean.setCard_no(consumerPaymentReqDTO.getCard_no());
                        chequeDDCardTransactionBean.setCard_type(consumerPaymentReqDTO.getCard_type());
                        chequeDDCardTransactionBean.setCard_holder_name(consumerPaymentReqDTO.getCard_holder_name());
                        chequeDDCardTransactionBean.setAmount(total_amount.setScale(2, RoundingMode.CEILING));
                        chequeDDCardTransactionBean.setCheque_status(1);
                        if (consumerPaymentReqDTO.getBank_name().equals("OTHERS")) {
                            chequeDDCardTransactionBean.setBank_name(consumerPaymentReqDTO.getOthers_bank_name());
                            BankNameBean bankNameBean = new BankNameBean();
                            bankNameBean.setBank_name(consumerPaymentReqDTO.getOthers_bank_name());
                            bankNameDao.save(bankNameBean);
                        } else {
                            chequeDDCardTransactionBean.setBank_name(consumerPaymentReqDTO.getBank_name());
                            chequeDDCardTransactionBean.setBranch_name(consumerPaymentReqDTO.getBranch());
                        }
                        chequeDDCardTransactionBean.setAmount(total_amount.setScale(2, RoundingMode.CEILING));
                        consumerTransactionDetailDao.save(chequeDDCardTransactionBean);
                    }
                    if (consumerPaymentReqDTO.getPayment_mode().equals("DD") || consumerPaymentReqDTO.getPayment_mode().equals("Dd")) {
                        ConsumerTransactionDetailBean chequeDDCardTransactionBean = new ConsumerTransactionDetailBean();
                        chequeDDCardTransactionBean.setTranscation_mstr_id(returnResult.getId());
                        chequeDDCardTransactionBean.setConsumer_detail_id(consumer_details_id);
                        chequeDDCardTransactionBean.setCheque_no(consumerPaymentReqDTO.getDd_no());
                        chequeDDCardTransactionBean.setCheque_date(consumerPaymentReqDTO.getDd_date());
                        chequeDDCardTransactionBean.setCheque_status(1);
                        chequeDDCardTransactionBean.setAmount(total_amount.setScale(2, RoundingMode.CEILING));
                        if (consumerPaymentReqDTO.getBank_name().equals("OTHERS")) {
                            chequeDDCardTransactionBean.setBank_name(consumerPaymentReqDTO.getOthers_bank_name());
                            BankNameBean bankNameBean = new BankNameBean();
                            bankNameBean.setBank_name(consumerPaymentReqDTO.getOthers_bank_name());
                            bankNameDao.save(bankNameBean);
                        } else {
                            chequeDDCardTransactionBean.setBank_name(consumerPaymentReqDTO.getBank_name());
                            chequeDDCardTransactionBean.setBranch_name(consumerPaymentReqDTO.getBranch());
                        }
                        chequeDDCardTransactionBean.setBranch_name(consumerPaymentReqDTO.getBranch());
                        chequeDDCardTransactionBean.setAmount(total_amount.setScale(2,RoundingMode.CEILING));
                        consumerTransactionDetailDao.save(chequeDDCardTransactionBean);
                    }
                    if (consumerPaymentReqDTO.getPayment_mode().equals("RTGS") || consumerPaymentReqDTO.getPayment_mode().equals("Rtgs")) {
                        ConsumerTransactionDetailBean chequeDDCardTransactionBean = new ConsumerTransactionDetailBean();
                        chequeDDCardTransactionBean.setTranscation_mstr_id(returnResult.getId());
                        chequeDDCardTransactionBean.setConsumer_detail_id(consumer_details_id);
                        chequeDDCardTransactionBean.setCheque_no(consumerPaymentReqDTO.getRtgs_no());
                        chequeDDCardTransactionBean.setCheque_date(consumerPaymentReqDTO.getRtgs_date());
                        chequeDDCardTransactionBean.setAmount(total_amount.setScale(2, RoundingMode.CEILING));
                        chequeDDCardTransactionBean.setCheque_status(1);
                        if (consumerPaymentReqDTO.getBank_name().equals("OTHERS")) {
                            chequeDDCardTransactionBean.setBank_name(consumerPaymentReqDTO.getOthers_bank_name());
                            BankNameBean bankNameBean = new BankNameBean();
                            bankNameBean.setBank_name(consumerPaymentReqDTO.getOthers_bank_name());
                            bankNameDao.save(bankNameBean);
                        } else {
                            chequeDDCardTransactionBean.setBank_name(consumerPaymentReqDTO.getBank_name());
                            chequeDDCardTransactionBean.setBranch_name(consumerPaymentReqDTO.getBranch());
                        }
                        chequeDDCardTransactionBean.setBranch_name(consumerPaymentReqDTO.getBranch());
                        chequeDDCardTransactionBean.setAmount(total_amount.setScale(2, RoundingMode.CEILING));
                        consumerTransactionDetailDao.save(chequeDDCardTransactionBean);
                    }
                    if (consumerPaymentReqDTO.getPayment_mode().equals("NEFT") || consumerPaymentReqDTO.getPayment_mode().equals("Neft")) {
                        ConsumerTransactionDetailBean chequeDDCardTransactionBean = new ConsumerTransactionDetailBean();
                        chequeDDCardTransactionBean.setTranscation_mstr_id(returnResult.getId());
                        chequeDDCardTransactionBean.setConsumer_detail_id(consumer_details_id);
                        chequeDDCardTransactionBean.setCheque_no(consumerPaymentReqDTO.getNeft_no());
                        chequeDDCardTransactionBean.setCheque_date(consumerPaymentReqDTO.getNeft_date());
                        chequeDDCardTransactionBean.setCheque_status(1);
                        chequeDDCardTransactionBean.setAmount(total_amount.setScale(2, RoundingMode.CEILING));
                        if (consumerPaymentReqDTO.getBank_name().equals("OTHERS")) {
                            chequeDDCardTransactionBean.setBank_name(consumerPaymentReqDTO.getOthers_bank_name());
                            BankNameBean bankNameBean = new BankNameBean();
                            bankNameBean.setBank_name(consumerPaymentReqDTO.getOthers_bank_name());
                            bankNameDao.save(bankNameBean);
                        } else {
                            chequeDDCardTransactionBean.setBank_name(consumerPaymentReqDTO.getBank_name());
                            chequeDDCardTransactionBean.setBranch_name(consumerPaymentReqDTO.getBranch());
                        }
                        chequeDDCardTransactionBean.setBranch_name(consumerPaymentReqDTO.getBranch());
                        chequeDDCardTransactionBean.setAmount(total_amount.setScale(2, RoundingMode.CEILING));
                        consumerTransactionDetailDao.save(chequeDDCardTransactionBean);
                    }
                }
               else {
                   throw new BadRequestException("Transaction Number not Generated");
                }
            }
            if( consumerPaymentReqDTO.getId()==null) {
                throw new BadRequestException("Id or DemandId shouldn't be blank");
            }

        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        return generateTransactionNo;
    }

    @Override
    public List<WasteCounterReportDTO> userChargeCounterReport(String dateFrom, String dateTo, String wardId, String userId, String paymentMode) {
        String jpql = "";
        Timestamp fromdate = Timestamp.valueOf(dateFrom + " 00:00:00.00");
        Timestamp todate = Timestamp.valueOf(dateTo + " 23:59:59.999999999");

        if(wardId.equals("") && userId.equals("") && paymentMode.equals("")){
            jpql = "SELECT c.holding_no,c.ward_no,b.consumer_no,b.consumer_name,b.mobile_no,a.transaction_date,a.transaction_no,a.payment_mode,a.payable_amt,d.cheque_no,d.cheque_date, d.bank_name, d.branch_name,e.name AS tax_collector FROM wastemgmt.tbl_transaction_master a join wastemgmt.tbl_consumer_details b on (a.consumer_detail_id=b.id) join wastemgmt.tbl_consumer_master c on (c.id=b.consumer_mstr_id) left join wastemgmt.tbl_transaction_details d on (d.transcation_mstr_id=a.id) JOIN public.tbl_user_details e ON a.user_id = e.user_id where a.stampdate between '" + fromdate + "' and '" + todate + "'";
        }
        else if(userId.equals("") && paymentMode.equals("")){
            jpql = "SELECT c.holding_no,c.ward_no,b.consumer_no,b.consumer_name,b.mobile_no,a.transaction_date,a.transaction_no,a.payment_mode,a.payable_amt,d.cheque_no,d.cheque_date, d.bank_name, d.branch_name,e.name AS tax_collector FROM wastemgmt.tbl_transaction_master a join wastemgmt.tbl_consumer_details b on (a.consumer_detail_id=b.id) join wastemgmt.tbl_consumer_master c on (c.id=b.consumer_mstr_id) left join wastemgmt.tbl_transaction_details d on (d.transcation_mstr_id=a.id) JOIN public.tbl_user_details e ON a.user_id = e.user_id where a.stampdate between '" + fromdate + "' and '" + todate + "' and a.ward_id='"+wardId+"'";
        }
        else if(paymentMode.equals("")){
            jpql = "SELECT c.holding_no,c.ward_no,b.consumer_no,b.consumer_name,b.mobile_no,a.transaction_date,a.transaction_no,a.payment_mode,a.payable_amt,d.cheque_no,d.cheque_date, d.bank_name, d.branch_name,e.name AS tax_collector FROM wastemgmt.tbl_transaction_master a join wastemgmt.tbl_consumer_details b on (a.consumer_detail_id=b.id) join wastemgmt.tbl_consumer_master c on (c.id=b.consumer_mstr_id) left join wastemgmt.tbl_transaction_details d on (d.transcation_mstr_id=a.id) JOIN public.tbl_user_details e ON a.user_id = e.user_id where a.stampdate between '" + fromdate + "' and '" + todate + "' and a.ward_id='"+wardId+"' and a.user_id='"+userId+"'";
        }
        else {
            jpql = "SELECT c.holding_no,c.ward_no,b.consumer_no,b.consumer_name,b.mobile_no,a.transaction_date,a.transaction_no,a.payment_mode,a.payable_amt,d.cheque_no,d.cheque_date, d.bank_name, d.branch_name,e.name AS tax_collector FROM wastemgmt.tbl_transaction_master a join wastemgmt.tbl_consumer_details b on (a.consumer_detail_id=b.id) join wastemgmt.tbl_consumer_master c on (c.id=b.consumer_mstr_id) left join wastemgmt.tbl_transaction_details d on (d.transcation_mstr_id=a.id) JOIN public.tbl_user_details e ON a.user_id = e.user_id where a.stampdate between '" + fromdate + "' and '" + todate + "' and a.ward_id='"+wardId+"' and a.user_id='"+userId+"' and a.payment_mode='"+paymentMode+"'";
        }
        Query query = entityManager.createNativeQuery(jpql);
        log.info("query................" + jpql);
        List<Object[]> results = query.getResultList();
        List<WasteCounterReportDTO> wasteCounterReportDTOS = new ArrayList<>();
        for (Object[] result : results) {
            WasteCounterReportDTO wasteCounterReportDTO = new WasteCounterReportDTO();
            wasteCounterReportDTO.setHolding_no((String) result[0]);
            wasteCounterReportDTO.setWard_no((BigInteger) result[1]);
            wasteCounterReportDTO.setConsumer_no((String) result[2]);
            wasteCounterReportDTO.setConsumer_name((String) result[3]);
            wasteCounterReportDTO.setMobile((BigInteger) result[4]);
            wasteCounterReportDTO.setTransaction_date((String) result[5]);
            wasteCounterReportDTO.setTransaction_no((String) result[6]);
            wasteCounterReportDTO.setPayment_mode((String) result[7]);
            wasteCounterReportDTO.setAmount((BigDecimal) result[8]);
            wasteCounterReportDTO.setCheque_no((String) result[9]);
            wasteCounterReportDTO.setCheque_date((String) result[10]);
            wasteCounterReportDTO.setBank_name((String) result[11]);
            wasteCounterReportDTO.setBranch_name((String) result[12]);
            wasteCounterReportDTO.setFrm_month("April"+"-"+(((String) result[5]).substring(0,4)));
            int to_month=Integer.parseInt(((String) result[5]).substring(0,4))+1;
            wasteCounterReportDTO.setTo_month("March"+"-"+to_month);
            wasteCounterReportDTO.setTax_collector((String) result[13]);
            wasteCounterReportDTOS.add(wasteCounterReportDTO);

        }
        return wasteCounterReportDTOS;
    }

    @Override
    public String consumerDetailsUpdate(ConsumerDetUpdateDTO consumerDetUpdateDTO) {
        ConsumerDetailsBean consumerDetailsBean = consumerDetailsDao.findConsumDetailsIdByNo(consumerDetUpdateDTO.getConsumer_no()).orElse(null);
        consumerDetailsBean.setConsumer_name(consumerDetUpdateDTO.getConsumer_name());
        consumerDetailsBean.setMobile_no(consumerDetUpdateDTO.getMobile_no());
        consumerDetailsDao.save(consumerDetailsBean);
        return "Update Successfully";
    }
}
