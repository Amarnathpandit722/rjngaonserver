package gov.municipal.suda.modules.property.service.transaction;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import gov.municipal.suda.modules.property.dao.transaction.CompositeTaxDao;
import gov.municipal.suda.modules.property.model.transaction.CompositeTax;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class CompositeTaxServiceImpl implements CompositeTaxService {

	@Autowired
	private CompositeTaxDao compositeTaxdao;
	
	
	
	
	
	@Override
	public List<CompositeTax> findByYear(String year) {
		List<CompositeTax> foundComposite = compositeTaxdao.findByYear(year);
		if(foundComposite!=null) {
			return foundComposite;
		}
		return null;
	}
	
	@Override
	public String findCalculationRateByTypeAndYear( String year, String type) {
		String rate = compositeTaxdao.findCalculationRateByTypeAndYear(year, type);
		log.info("RATE FROM CompositeTaxServiceImpl ===========>>>>>>>>>>",rate);
		if(rate!=null) {
			return rate;
		}
		return null;
	}

}
