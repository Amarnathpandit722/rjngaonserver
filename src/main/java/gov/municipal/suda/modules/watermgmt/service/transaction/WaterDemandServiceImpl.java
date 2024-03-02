package gov.municipal.suda.modules.watermgmt.service.transaction;

import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.modules.watermgmt.dao.master.ConnectionTypeMasterDao;
import gov.municipal.suda.modules.watermgmt.dao.master.WaterConsumerDetailsDao;
import gov.municipal.suda.modules.watermgmt.dao.transaction.DemandDetailsWaterDao;
import gov.municipal.suda.modules.watermgmt.dao.transaction.WaterDemandPrintLogDao;
import gov.municipal.suda.modules.watermgmt.dto.transaction.WaterDemandPrintLogDto;
import gov.municipal.suda.modules.watermgmt.model.master.WaterConsumerDetailsBean;
import gov.municipal.suda.modules.watermgmt.model.transaction.WaterDemandLogPrintBean;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
@Slf4j
public class WaterDemandServiceImpl implements WaterDemandService{
    @Autowired
    DemandDetailsWaterDao demandDetailsWaterDao;
    @Autowired
    WaterConsumerDetailsDao waterConsumerDetailsDao;
    @Autowired
    ConnectionTypeMasterDao connectionTypeMasterDao;

    @Autowired
    WaterDemandPrintLogDao demandPrintLogDao;

    @Override
    public void generateSingleDemand(String consumerNo) {
        WaterConsumerDetailsBean consumerDetailsBean=waterConsumerDetailsDao.findIdByConsumerNo(consumerNo);
        Long consumerDetailsId=0L;
        if(consumerDetailsBean !=null) {
           consumerDetailsId=consumerDetailsBean.getId();

        }
        else if(consumerDetailsBean==null) {
            throw new BadRequestException("Consumer No not found");
        }

    }

    @Override
//    @Transactional
//            (rollbackFor = Exception.class,
//                    noRollbackFor = EntityNotFoundException.class)
    public void demandPrintLogEntry(WaterDemandPrintLogDto dto) {
        try {
            WaterDemandLogPrintBean bean = new WaterDemandLogPrintBean();
            bean.setDemand_from(dto.getDemand_from());
            bean.setDemand_upto(dto.getDemand_upto());
            bean.setConsumer_dets_id(dto.getConsumer_dets_id());
            bean.setWard_id(dto.getWard_id());
            bean.setTot_amount(dto.getTot_amount());
            bean.setOld_ward_id(dto.getOld_ward_id());
            bean.setEntry_date(LocalDate.now().toString());
            bean.setEntry_time(LocalTime.now().withNano(0).toString());
            bean.setUser_id(dto.getUser_id());
            demandPrintLogDao.save(bean);
        }
        catch(Exception e) {
            log.info(e.getMessage());
            throw new BadRequestException("Data not saved for Demand Print Log Entry");
        }
    }
}
