package gov.municipal.suda.modules.property.service.master;

import java.math.BigDecimal;
import java.util.List;

public interface ArvRangeService {

    List<Object> getAllArvRange();
    public BigDecimal findPercentageByNumberAndDate(long num , String effectDate);
}
