package gov.municipal.suda.modules.watermgmt.dto.transaction;

import gov.municipal.suda.modules.watermgmt.model.master.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WaterAllDropDownDto {
    private List<BPLAPLDetailsBean> aplBpl;
    private List<ConnectionTypeMasterBean> connectionType;
    private List<PropertyTypeWaterMasterBean> propertyType;
    private List<WaterModeOfPaymentBean> modeOfPayment;
    private List<WaterRangeMasterBean> range;
    private List<WaterRateMasterBean> rate;

}
