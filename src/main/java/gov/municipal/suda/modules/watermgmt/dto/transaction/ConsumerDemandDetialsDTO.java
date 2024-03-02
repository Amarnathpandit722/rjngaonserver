package gov.municipal.suda.modules.watermgmt.dto.transaction;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ConsumerDemandDetialsDTO {

	public BigDecimal water_ArrearAmount;
	public BigDecimal water_CurrentAmount;
	
	
	
}
