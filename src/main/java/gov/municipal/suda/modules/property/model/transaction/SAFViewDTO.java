package gov.municipal.suda.modules.property.model.transaction;

import gov.municipal.suda.modules.property.model.master.OwnerDetailsBean;
import gov.municipal.suda.modules.property.model.master.PropertyMasterBean;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SAFViewDTO {
    private List<PropertyMasterBean> propertyMasterBean;
    private List<OwnerDetailsBean> ownerDetailsBean;

    private List<SAFARVDetailsBean> safarvDetailsBean;
}
