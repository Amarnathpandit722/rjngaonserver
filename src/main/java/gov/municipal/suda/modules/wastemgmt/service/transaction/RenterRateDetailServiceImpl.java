package gov.municipal.suda.modules.wastemgmt.service.transaction;

import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.modules.wastemgmt.dao.transaction.ConsumerRateDetailsDao;
import gov.municipal.suda.modules.wastemgmt.dto.AreaDetailsDto;
import gov.municipal.suda.modules.wastemgmt.dto.RenterEntryDTO;
import gov.municipal.suda.modules.wastemgmt.model.transaction.ConsumerRateDetailsBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Slf4j
public class RenterRateDetailServiceImpl implements RenterRateDetailService{
    @Autowired
    ConsumerRateDetailsDao consumerRateDetailsDao;
    @Override
    @Transactional(rollbackOn= BadRequestException.class)
    public String addRenterRate(Long id, RenterEntryDTO dto) {
        ConsumerRateDetailsBean consumerRateDetEntry=new ConsumerRateDetailsBean();
        if(!dto.getArea_details().isEmpty()) {
            for (AreaDetailsDto renterEntryDTO : dto.getArea_details()) {
                consumerRateDetEntry.setConsumer_details_id(id);
                consumerRateDetEntry.setConsumer_cat_mstr_id(renterEntryDTO.getConsumer_cat_mstr_id());
                consumerRateDetEntry.setConsumer_range_mstr_id(renterEntryDTO.getConsumer_range_mstr_id());
                consumerRateDetEntry.setFinancial_year(renterEntryDTO.getFinancial_year());
                consumerRateDetEntry.setMonth(renterEntryDTO.getMonth());
                consumerRateDetEntry.setDoe(LocalDate.now().toString());
                consumerRateDetEntry.setStampdatetime(Timestamp.valueOf(LocalDateTime.now()));
                consumerRateDetEntry.setStatus(1);
                consumerRateDetEntry.setConsumer_rate_mstr_id(renterEntryDTO.getConsumer_rate_mstr_id());
                consumerRateDetEntry.setFinancial_year_id(renterEntryDTO.getFinancial_year_id());
                //consumerRateDetEntry.setNoof_restaurant(0);

                try {
                    consumerRateDetailsDao.save(consumerRateDetEntry);
                } catch (BadRequestException e) {

                    return new BadRequestException("Data not save ").toString();
                }
            }
        }
        return "Success";
    }
}
