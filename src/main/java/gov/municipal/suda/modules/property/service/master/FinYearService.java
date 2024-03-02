package gov.municipal.suda.modules.property.service.master;

import gov.municipal.suda.modules.property.model.master.FinYearBean;

import java.util.List;
import java.util.Optional;

public interface FinYearService {

    Optional<List<FinYearBean>> findAllFinYear();

    List<FinYearBean> getFinancialYear();
}
