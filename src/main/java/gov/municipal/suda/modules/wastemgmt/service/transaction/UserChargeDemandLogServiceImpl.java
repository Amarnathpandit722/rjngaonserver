package gov.municipal.suda.modules.wastemgmt.service.transaction;

import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.modules.wastemgmt.dao.transaction.UserChargeDemandLogDao;
import gov.municipal.suda.modules.wastemgmt.dto.UserChargeDemandLogDTO;
import gov.municipal.suda.modules.wastemgmt.model.transaction.UserChargeDemandLogBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.nio.channels.ScatteringByteChannel;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Slf4j
public class UserChargeDemandLogServiceImpl implements  UserChargeDemandLogService{
    @Autowired
    UserChargeDemandLogDao userChargeDemandLogDao;
    @Override
    @Transactional(rollbackOn= BadRequestException.class)
    public void createDemandLog(UserChargeDemandLogDTO dto) {
        if(dto !=null) {
            try {
                UserChargeDemandLogBean bean = new UserChargeDemandLogBean();
                bean.setConsumer_detail_id(dto.getConsumer_detail_id());
                bean.setTotal_amt(dto.getTotal_amt());
                bean.setUser_id(dto.getUser_id());
                bean.setStampdate(LocalDate.now().toString());
                bean.setEntry_time(LocalDateTime.now().toLocalTime().withNano(0).toString());
                bean.setFromdate(dto.getFromdate());
                bean.setTodate(dto.getTodate());
                UserChargeDemandLogBean result=userChargeDemandLogDao.save(bean);
                //log.info(result.toString());
            }
            catch(Exception e)  {
                log.info(e.getMessage());
            throw new BadRequestException(e.getMessage());
            }
        }


    }
}
