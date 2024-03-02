package gov.municipal.suda.modules.property.service.master;

import gov.municipal.suda.modules.property.dao.master.ArvRangeDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class ArvRangeServiceImpl implements ArvRangeService{

    @Autowired
    ArvRangeDao arvRangeDao;

    @Override
    public List<Object> getAllArvRange() {
        return arvRangeDao.getAllRange();
    }
    
    public BigDecimal findPercentageByNumberAndDate(long num, String effectDate) {
        BigDecimal percent = arvRangeDao.findPercentageByNumberAndDate(num, effectDate);
        if(!percent.equals(BigDecimal.ZERO)) {
            return percent;
        }
        return null;
    }

}
