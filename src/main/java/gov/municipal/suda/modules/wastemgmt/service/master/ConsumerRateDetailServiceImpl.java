package gov.municipal.suda.modules.wastemgmt.service.master;

import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.modules.wastemgmt.dao.master.ConsumerRateChartDao;
import gov.municipal.suda.modules.wastemgmt.dao.transaction.ConsumerDemandDao;
import gov.municipal.suda.modules.wastemgmt.dao.master.ConsumerDetailsDao;
import gov.municipal.suda.modules.wastemgmt.dao.transaction.ConsumerRateDetailsDao;
import gov.municipal.suda.modules.wastemgmt.dto.AreaDetailsDto;
import gov.municipal.suda.modules.wastemgmt.dto.ConsumerRateDetailsDto;
import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerDetailsBean;
import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerRateChartBean;
import gov.municipal.suda.modules.wastemgmt.model.transaction.ConsumerDemandBean;
import gov.municipal.suda.modules.wastemgmt.model.transaction.ConsumerRateDetailsBean;
import gov.municipal.suda.modules.wastemgmt.service.transaction.ConsumerDemandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ConsumerRateDetailServiceImpl implements ConsumerRateDetailService{
    @Autowired
    ConsumerRateDetailsDao consumerRateDetailsDao;
    @Autowired
    ConsumerDemandService demandService;
    @Autowired
    ConsumerRateChartDao rateChartDao;
    @Autowired
    ConsumerDetailsDao consumerDetailsDao;
    @Autowired
    ConsumerDemandDao demandDao;
    @Autowired
    ConsumerDemandService consumerDemandService;
    @Override
    @Transactional(rollbackOn= BadRequestException.class)
    public String addConsumerRate( Long consumerMasterId, List<AreaDetailsDto> areDetails) {
        if(consumerMasterId!=null) {
            for (AreaDetailsDto areaDetail : areDetails) {
                Optional<ConsumerRateChartBean> rateChart=rateChartDao.findById(areaDetail.getRate_chart_id());
                ConsumerRateDetailsBean consumerRateDetEntry = new ConsumerRateDetailsBean();
                consumerRateDetEntry.setConsumer_details_id(consumerMasterId);
                consumerRateDetEntry.setConsumer_cat_mstr_id(areaDetail.getConsumer_cat_mstr_id());
                consumerRateDetEntry.setConsumer_range_mstr_id(areaDetail.getConsumer_range_mstr_id());
                consumerRateDetEntry.setFinancial_year(areaDetail.getFinancial_year());
                consumerRateDetEntry.setMonth(areaDetail.getMonth());
                consumerRateDetEntry.setDoe(rateChart.get().getFee_effectdate().substring(0,10));
                consumerRateDetEntry.setStampdatetime(Timestamp.valueOf(LocalDateTime.now()));
                consumerRateDetEntry.setStatus(1);
                consumerRateDetEntry.setConsumer_rate_mstr_id(areaDetail.getRate_chart_id());
                consumerRateDetEntry.setFinancial_year_id(areaDetail.getFinancial_year_id());
                consumerRateDetEntry.setNoof_restaurant(0L);
                consumerRateDetEntry.setNoof_garden(0L);
                consumerRateDetEntry.setNoof_banquethall(0L);
                consumerRateDetEntry.setNoof_sqft_truck_room(new BigDecimal(0.00));
                try {
                    consumerRateDetailsDao.save(consumerRateDetEntry);
                } catch (BadRequestException e) {

                    return new BadRequestException("Data not save ").toString();
                }
            }
        }
        else if(consumerMasterId==null) {
            throw new BadRequestException("Consumer not present");
        }
        return "Success";
    }

    @Override
    public void consumerRateDetailsUpdate(String consumerNo, Long rateId, Long previousChartId, AreaDetailsDto areaDetailsDto){
        List<AreaDetailsDto> areaDetails=new ArrayList<>();
        areaDetails.add(areaDetailsDto);
        if(rateId !=null) {
            Optional<ConsumerRateDetailsBean> rateDetails = consumerRateDetailsDao.findById(rateId);
            if(rateDetails.isPresent()) {
                ConsumerRateDetailsBean rateDao=rateDetails.get();
                rateDao.setConsumer_cat_mstr_id(areaDetailsDto.getConsumer_cat_mstr_id());
                rateDao.setConsumer_range_mstr_id(areaDetailsDto.getConsumer_range_mstr_id());
                rateDao.setConsumer_rate_mstr_id(areaDetailsDto.getRate_chart_id());
                rateDao.setFinancial_year_id(areaDetailsDto.getFinancial_year_id());
                rateDao.setFinancial_year(areaDetailsDto.getFinancial_year());
                rateDao.setMonth(areaDetailsDto.getMonth());
                ConsumerRateDetailsBean returnResult=consumerRateDetailsDao.save(rateDao);

                if(returnResult!=null) {
                 Optional<ConsumerDetailsBean> findConsumerMasterId = consumerDetailsDao.findConsumerDetailsByConsumerNo(consumerNo);
                 if(findConsumerMasterId.isPresent()) {
                     List<ConsumerDemandBean> pendingDemand = demandDao.fetchConsumerDueDemandByDtlIdAndRateChartId(findConsumerMasterId.get().getId(),previousChartId);
                     Optional<ConsumerRateChartBean> findRateDetailsChart=rateChartDao.findById(areaDetailsDto.getRate_chart_id());
                     if (findRateDetailsChart.isPresent()) {
                             for (ConsumerDemandBean demandSave : pendingDemand) {
                                demandSave.setDemand_amount(findRateDetailsChart.get().getAmount());
                                demandSave.setRate_chart_id(areaDetailsDto.getRate_chart_id());
                                demandDao.save(demandSave);
                             }
                     }
                 }
                }
               // demandService.createConsumerDemand(rateDao.getConsumer_details_id(), areaDetails);

            }
        }
    }
    @Override
    public void consumerCategoryEntryInRateDetailsByConsumerDetailsId(ConsumerRateDetailsDto consumerRateDetailsDto) {
    Integer duplicateCount=consumerRateDetailsDao.checkDuplicateCategory(consumerRateDetailsDto.getConsumer_details_id(),consumerRateDetailsDto.getConsumer_cat_mstr_id(),consumerRateDetailsDto.getConsumer_range_mstr_id());
    //Optional<ConsumerDetailsBean> findConsumerMasterId = consumerDetailsDao.findConsumerDetailsByConsumerMasterId(consumerRateDetailsDto.getConsumer_details_id());

    if(duplicateCount>0) {
        throw new BadRequestException("Duplicate Category Entry");
    }
    ConsumerRateDetailsBean dto = new ConsumerRateDetailsBean();
    dto.setConsumer_details_id(consumerRateDetailsDto.getConsumer_details_id()); // in the Consumer Rate Details, consumerDetailsId field value is actually consumerMasterId value
    dto.setConsumer_cat_mstr_id(consumerRateDetailsDto.getConsumer_cat_mstr_id());
    dto.setConsumer_range_mstr_id(consumerRateDetailsDto.getConsumer_range_mstr_id());
    dto.setFinancial_year_id(consumerRateDetailsDto.getFinancial_year_id());
    dto.setMonth(consumerRateDetailsDto.getMonth());
    dto.setFinancial_year(consumerRateDetailsDto.getFinancial_year());
    dto.setStatus(1);
    dto.setDoe(LocalDate.now().toString());
    dto.setConsumer_rate_mstr_id(consumerRateDetailsDto.getConsumer_rate_mstr_id());
    dto.setStampdatetime(Timestamp.from(Instant.now()));
    dto.setNoof_restaurant(0L);
    dto.setNoof_garden(0L);
    dto.setNoof_banquethall(0L);
    dto.setNoof_sqft_truck_room(new BigDecimal(1.00));
    ConsumerRateDetailsBean results=consumerRateDetailsDao.save(dto);
    if(results.getId() !=null) {
        Optional<ConsumerDetailsBean> consumerDetails =consumerDetailsDao.findConsumerDetailsByConsumerMasterId(results.getConsumer_details_id());
//        List<ConsumerDemandBean> pendingDemand=demandDao.fetchConsumerDueDemandByDtlId(consumerDetails.get().getId());
//        BigDecimal currentDemandAmount=consumerRateDetailsDto.getMonthlyAmount().multiply(new BigDecimal(consumerRateDetailsDto.getNoOfRoomTable()));
//        if(pendingDemand.size()>0) {
//            for (ConsumerDemandBean demandBean : pendingDemand) {
//                BigDecimal finalDemandAmount=demandBean.getDemand_amount().add(currentDemandAmount);
//                demandBean.setRate_chart_id(consumerRateDetailsDto.getRate_chart_id());
//                demandBean.setDemand_amount(finalDemandAmount);
//                demandDao.save(demandBean);
//            }
//        }

        consumerDemandService.singleDemandCreationByConsumerDetailsId(consumerDetails.get().getId(),consumerRateDetailsDto);
    }
    }
}
