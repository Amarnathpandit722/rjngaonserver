package gov.municipal.suda.modules.wastemgmt.service.transaction;

import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.modules.wastemgmt.dao.transaction.ConsumerDemandDao;
import gov.municipal.suda.modules.wastemgmt.dao.master.ConsumerRateChartDao;
import gov.municipal.suda.modules.wastemgmt.dto.AreaDetailsDto;
import gov.municipal.suda.modules.wastemgmt.dto.RenterEntryDTO;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RenterDemandServiceImpl implements RenterDemandService{
    @Autowired
    ConsumerDemandDao consumerDemandDao;
    @Autowired
    ConsumerRateChartDao consumerRateChartDao;
    @Override
    @Transactional(rollbackOn= BadRequestException.class)
    public String createRenterDemand(Long id, RenterEntryDTO renterEntryDTO) {
        for(AreaDetailsDto demandDto: renterEntryDTO.getArea_details()) {
            String fromYear = demandDto.getFinancial_year().substring(0, 4);
            List<ConsumerRateChartBean> monthlyAmount = consumerRateChartDao.fetchMonthlyAmount(Long.valueOf(demandDto.getConsumer_range_mstr_id()));
            Optional<ConsumerRateChartBean> latestObject = monthlyAmount.stream()
                    .sorted(Comparator.comparing(ConsumerRateChartBean::getAmount).reversed()) // sort by stampdate in descending order
                    .findFirst();
            LocalDate startDate = LocalDate.of(Integer.parseInt(fromYear), Month.of(Math.toIntExact(demandDto.getMonth())), 1);
            LocalDate endDate = LocalDate.of(LocalDate.now().getYear() + 1, Month.MARCH, 31);
            try {
                for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusMonths(1)) {
                    ConsumerDemandBean consumerDemandEntry = new ConsumerDemandBean();
                    YearMonth yearMonth = YearMonth.from(date);
                    consumerDemandEntry.setConsumer_detail_id(id);
                    consumerDemandEntry.setWard_id(demandDto.getWard_id());
                    consumerDemandEntry.setDemand_from(String.valueOf(yearMonth.atDay(1)));
                    consumerDemandEntry.setDemand_to(String.valueOf(yearMonth.atEndOfMonth()));
                    consumerDemandEntry.setDemand_amount(latestObject.get().getAmount());
                    consumerDemandEntry.setFinancial_year(demandDto.getFinancial_year());
                    consumerDemandEntry.setRate_chart_id(demandDto.getRate_chart_id());
                    consumerDemandEntry.setStampdate(LocalDate.now().toString());
                    consumerDemandEntry.setPayment_status(0L);
                    consumerDemandEntry.setStatus(1);
                    consumerDemandEntry.setLast_payment_id(0L);
                    consumerDemandEntry.setFinancial_year_id(demandDto.getFinancial_year_id());
                    consumerDemandEntry.setUser_id(BigDecimal.valueOf(demandDto.getCreated_byid()));
                    consumerDemandDao.save(consumerDemandEntry);
                }
            } catch (BadRequestException e) {

                return new BadRequestException("Data not save ").toString();
            }
        }
        return "Success";
    }
}
