package gov.municipal.suda.modules.property.dto;

import gov.municipal.suda.modules.property.model.master.OwnerTaxMasterBean;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewDemandRequestDTO {

    OwnerTaxMasterBean ownerTaxMasterRequest;
}
