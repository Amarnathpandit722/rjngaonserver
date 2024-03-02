package gov.municipal.suda.modules.property.dto;

import gov.municipal.suda.modules.property.model.master.OwnerTaxMasterBean;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@ToString
public class OwnerTaxMasterDTO {

    List<OwnerTaxMasterBean> masterBeans;

}
