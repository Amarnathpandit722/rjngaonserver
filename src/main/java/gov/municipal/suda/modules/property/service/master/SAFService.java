package gov.municipal.suda.modules.property.service.master;

import gov.municipal.suda.modules.property.model.master.PropertyMasterBean;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SAFService {
    List<PropertyMasterBean> findAllByPage(Pageable page);
}
