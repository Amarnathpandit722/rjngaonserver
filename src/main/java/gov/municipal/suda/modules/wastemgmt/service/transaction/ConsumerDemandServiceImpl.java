package gov.municipal.suda.modules.wastemgmt.service.transaction;

import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.modules.property.dao.master.FinYearDao;
import gov.municipal.suda.modules.property.dao.master.WardDao;
import gov.municipal.suda.modules.property.model.master.FinYearBean;
import gov.municipal.suda.modules.property.model.master.WardBean;
import gov.municipal.suda.modules.wastemgmt.dao.transaction.ConsumerDemandDao;
import gov.municipal.suda.modules.wastemgmt.dao.master.ConsumerRateChartDao;
import gov.municipal.suda.modules.wastemgmt.dto.AreaDetailsDto;
import gov.municipal.suda.modules.wastemgmt.dto.UserChargeDemandLogDTO;
import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerRateChartBean;
import gov.municipal.suda.modules.wastemgmt.model.transaction.ConsumerDemandBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ConsumerDemandServiceImpl implements ConsumerDemandService {
    @Autowired
    ConsumerDemandDao consumerDemandDao;
    @Autowired
    ConsumerRateChartDao consumerRateChartDao;

    @Autowired
    WardDao wardDao;

    @Autowired
    FinYearDao finYearDao;

    @Autowired
    UserChargeDemandLogService userChargeDemandLogService;
    @Override
    @Transactional(rollbackOn = BadRequestException.class)
    public String createConsumerDemand(Long id, List<AreaDetailsDto> areaDetails) {




        for(AreaDetailsDto demandDto: areaDetails) {
            ConsumerDemandBean demandBean=null;
            List<YearMonth> listOfYearAndMonth=new ArrayList<>();
            BigDecimal totalDemand=new BigDecimal(0.00);
            String fromYear = demandDto.getFinancial_year().substring(0, 4);
            List<ConsumerRateChartBean> monthlyAmount = consumerRateChartDao.fetchMonthlyAmountByRateId(Long.valueOf(demandDto.getRate_chart_id()));
            Optional<ConsumerRateChartBean> latestObject = monthlyAmount.stream()
                    .sorted(Comparator.comparing(ConsumerRateChartBean::getAmount).reversed()) // sort by stampdate in descending order
                    .findFirst();
            LocalDate startDate = LocalDate.of(Integer.parseInt(fromYear), Month.of(Math.toIntExact( demandDto.getMonth())), 1);
            LocalDate endDate = LocalDate.now();
            if(LocalDate.now().getMonth().getValue()>3) {
                endDate = LocalDate.of(LocalDate.now().getYear() + 1, Month.MARCH, 31);
            } else if (LocalDate.now().getMonth().getValue()>0 && LocalDate.now().getMonth().getValue()<4) {
                endDate = LocalDate.of(LocalDate.now().getYear(), Month.MARCH, 31);
            }

            try {
                   for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusMonths(1)) {
                       String finYearFromDB=null;
                       Long finYearIdFromDB=null;

                       totalDemand =totalDemand.add(latestObject.get().getAmount());
                       ConsumerDemandBean consumerDemandEntry = new ConsumerDemandBean();
                       YearMonth yearMonth = YearMonth.from(date);
                       listOfYearAndMonth.add(YearMonth.from(yearMonth.atDay(1)));
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
                       consumerDemandEntry.setConsumer_detail_id(id);
                       consumerDemandEntry.setWard_id(demandDto.getWard_id());
                       consumerDemandEntry.setDemand_from(String.valueOf(yearMonth.atDay(1)));
                       consumerDemandEntry.setDemand_to(String.valueOf(yearMonth.atEndOfMonth()));
                       consumerDemandEntry.setDemand_amount(latestObject.get().getAmount());
                       consumerDemandEntry.setFinancial_year(finYearFromDB);
                       consumerDemandEntry.setFinancial_year_id(finYearIdFromDB);
                       consumerDemandEntry.setRate_chart_id(demandDto.getRate_chart_id());
                       consumerDemandEntry.setStampdate(LocalDate.now().toString());
                       consumerDemandEntry.setPayment_status(0L);
                       consumerDemandEntry.setStatus(1);
                       consumerDemandEntry.setLast_payment_id(0L);

                       consumerDemandEntry.setUser_id(BigDecimal.valueOf(demandDto.getCreated_byid()));
                       consumerDemandEntry.setOld_ward_id(demandDto.getWard_id());
                       demandBean= consumerDemandDao.save(consumerDemandEntry);

                   }
                if(demandBean.getId() !=null) {
                    UserChargeDemandLogDTO demandLogDTO = new UserChargeDemandLogDTO();
                    if(listOfYearAndMonth.size() > 0) {
                        demandLogDTO.setConsumer_detail_id(id);
                        listOfYearAndMonth.sort(Comparator.naturalOrder());
                        demandLogDTO.setFromdate(listOfYearAndMonth.get(0).atDay(1).toString());
                        demandLogDTO.setTodate(listOfYearAndMonth.get(listOfYearAndMonth.size() - 1).atEndOfMonth().toString());
                        demandLogDTO.setTotal_amt(totalDemand);
                        demandLogDTO.setUser_id(demandDto.getCreated_byid());
                        userChargeDemandLogService.createDemandLog(demandLogDTO);
                    }
                }
                } catch(BadRequestException e){
                    return new BadRequestException("Data not save ").toString();
                }

            }

        return "Success";
    }

    @Override
    public String singleDemandCreationByConsumerDetailsId(Long consumerDetailsId, AreaDetailsDto demandDto) {
        String finYearFromDB=null;
        Long finYearIdFromDB=null;
        List<YearMonth> listOfYearAndMonth=new ArrayList<>();
        ConsumerDemandBean demandBean=null;
        BigDecimal totalDemand=new BigDecimal(0.00);
        String fromYear = demandDto.getFinancial_year().substring(0, 4);
        List<WardBean> financialYearList=wardDao.findAll();
        LocalDate currentDate= LocalDate.now();
        List<ConsumerDemandBean> generatedDemand = consumerDemandDao.fetchGeneratedDemandByConsumerDetailsId(consumerDetailsId);
        String latestGeneratedDemandDate=generatedDemand.get(0).getDemand_to();
        if(!generatedDemand.isEmpty()) {
            if(Integer.parseInt(fromYear)>Integer.parseInt(latestGeneratedDemandDate.substring(0,4))) {

            }
        }
        List<ConsumerRateChartBean> monthlyAmount = consumerRateChartDao.fetchMonthlyAmountByRateId(Long.valueOf(demandDto.getRate_chart_id()));
        Optional<ConsumerRateChartBean> latestObject = monthlyAmount.stream()
                .sorted(Comparator.comparing(ConsumerRateChartBean::getAmount).reversed()) // sort by stampdate in descending order
                .findFirst();
        LocalDate startDate = LocalDate.of(Integer.parseInt(fromYear), Month.of(Math.toIntExact(demandDto.getMonth())), 1);
        LocalDate endDate = LocalDate.now();
        if(LocalDate.now().getMonth().getValue()>3) {
            endDate = LocalDate.of(LocalDate.now().getYear() + 1, Month.MARCH, 31);
        } else if (LocalDate.now().getMonth().getValue()>0 && LocalDate.now().getMonth().getValue()<4) {
            endDate = LocalDate.of(LocalDate.now().getYear(), Month.MARCH, 31);
        }

        try {
            for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusMonths(1)) {
                totalDemand =totalDemand.add(latestObject.get().getAmount());
                ConsumerDemandBean consumerDemandEntry = new ConsumerDemandBean();
                YearMonth yearMonth = YearMonth.from(date);
                listOfYearAndMonth.add(YearMonth.from(yearMonth.atDay(1)));
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
                consumerDemandEntry.setConsumer_detail_id(consumerDetailsId);
                consumerDemandEntry.setWard_id(demandDto.getWard_id());
                consumerDemandEntry.setDemand_from(String.valueOf(yearMonth.atDay(1)));
                consumerDemandEntry.setDemand_to(String.valueOf(yearMonth.atEndOfMonth()));
                consumerDemandEntry.setDemand_amount(latestObject.get().getAmount());
                consumerDemandEntry.setFinancial_year(finYearFromDB);
                consumerDemandEntry.setFinancial_year_id(finYearIdFromDB);
                consumerDemandEntry.setRate_chart_id(demandDto.getRate_chart_id());
                consumerDemandEntry.setStampdate(LocalDate.now().toString());
                consumerDemandEntry.setPayment_status(0L);
                consumerDemandEntry.setStatus(1);
                consumerDemandEntry.setLast_payment_id(0L);

                consumerDemandEntry.setUser_id(BigDecimal.valueOf(demandDto.getCreated_byid()));
                consumerDemandEntry.setOld_ward_id(demandDto.getWard_id());
                demandBean=consumerDemandDao.save(consumerDemandEntry);
            }

            if(demandBean.getId() !=null) {
                UserChargeDemandLogDTO demandLogDTO = new UserChargeDemandLogDTO();
                if(listOfYearAndMonth.size() > 0) {
                    demandLogDTO.setConsumer_detail_id(consumerDetailsId);
                    listOfYearAndMonth.sort(Comparator.naturalOrder());
                    demandLogDTO.setFromdate(listOfYearAndMonth.get(0).atDay(1).toString());
                    demandLogDTO.setTodate(listOfYearAndMonth.get(listOfYearAndMonth.size() - 1).atEndOfMonth().toString());
                    demandLogDTO.setTotal_amt(totalDemand);
                    demandLogDTO.setUser_id(demandDto.getCreated_byid());
                    userChargeDemandLogService.createDemandLog(demandLogDTO);
                }
            }
        } catch(BadRequestException e){
            return new BadRequestException("Data not save ").toString();
        }
        return "Success";
    }

    @Override
    public String updateConsumerDemand(Long consumerDetailsId, AreaDetailsDto areaDetails) {
        return null;
    }

    @Override
    public String updateLastPaymentUpdate(Long consumerDetailsId, String demandFrom, String demandTo) {
        return null;
    }
}
