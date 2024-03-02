package gov.municipal.suda.modules.property.service.master;

import gov.municipal.suda.modules.property.model.master.SAFAllDropDownBean;
import java.util.Optional;

public interface SAFAllDropDownService {
    Optional<SAFAllDropDownBean> findAllDropDown();
}
