package gov.municipal.suda.modules.property.service.transaction;

import java.util.List;

import gov.municipal.suda.modules.property.model.transaction.CompositeTax;

public interface CompositeTaxService {

	public List<CompositeTax> findByYear(String year);
	
	public String findCalculationRateByTypeAndYear( String year, String type);
	
}
