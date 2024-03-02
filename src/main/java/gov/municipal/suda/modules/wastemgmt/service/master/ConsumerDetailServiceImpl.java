package gov.municipal.suda.modules.wastemgmt.service.master;

import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.modules.wastemgmt.dao.master.ConsumerDetailsDao;
import gov.municipal.suda.modules.wastemgmt.dto.ConsumerEntryDTO;
import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerDetailsBean;
import gov.municipal.suda.modules.wastemgmt.service.transaction.ConsumerDemandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
@Service
@Slf4j
public class ConsumerDetailServiceImpl implements ConsumerDetailService{
    @Autowired
    ConsumerDetailsDao consumerDetailsDao;
    @Autowired
    ConsumerRateDetailService consumerRateDetailService;
    @Autowired
    ConsumerDemandService consumerDemandService;
    @Override
    @Transactional(rollbackOn= BadRequestException.class)
    public String addConsumerDetails(Long id, ConsumerEntryDTO consumerEntryDTO,String consumer_no) {
        ConsumerDetailsBean consumerDetailEntry=new ConsumerDetailsBean();
        consumerDetailEntry.setConsumer_mstr_id(id);
        consumerDetailEntry.setConsumer_no(consumer_no);
        consumerDetailEntry.setConsumer_name(consumerEntryDTO.getConsumer_name());
        consumerDetailEntry.setMobile_no(consumerEntryDTO.getMobile_no());
        consumerDetailEntry.setHouse_flat_no(consumerEntryDTO.getHouse_flat_no());
        consumerDetailEntry.setGradian_name(consumerEntryDTO.getGradian_name());
        consumerDetailEntry.setCreated_byid(consumerEntryDTO.getCreated_byid());
        consumerDetailEntry.setStampdate(Timestamp.valueOf(LocalDateTime.now()));
        consumerDetailEntry.setStatus(1);
        consumerDetailEntry.setRelation(consumerEntryDTO.getRelation());
        consumerDetailEntry.setConsumer_type("Self");
        consumerDetailEntry.setConsumer_details_id(0L);
        try {
            ConsumerDetailsBean consumerDetView=consumerDetailsDao.save(consumerDetailEntry);
            consumerRateDetailService.addConsumerRate(consumerDetView.getConsumer_mstr_id(),consumerEntryDTO.getArea_details());
            consumerDemandService.createConsumerDemand(consumerDetView.getId(),consumerEntryDTO.getArea_details());
        }
        catch (BadRequestException e) {

            return new BadRequestException("Data not save ").toString();
        }
        return "Success";
    }
}
