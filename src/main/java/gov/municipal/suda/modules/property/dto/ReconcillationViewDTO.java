package gov.municipal.suda.modules.property.dto;

import gov.municipal.suda.modules.property.model.transaction.ChequeDDCardTransactionBean;
import gov.municipal.suda.modules.property.model.transaction.PropertyTransactionBean;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReconcillationViewDTO {

    private List<PropertyTransactionBean> propertyTransactionBeans;
    private List<ChequeDDCardTransactionBean> chequeDDCardTransactionBeans;
}
