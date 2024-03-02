package gov.municipal.suda.modules.property.dao.transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import gov.municipal.suda.modules.property.model.transaction.CompositeTax;

public interface CompositeTaxDao extends JpaRepository<CompositeTax, Long> {


	@Query("SELECT ct FROM CompositeTax ct WHERE ct.year = ?1 AND ct.status = 1")
	List<CompositeTax> findByYear(String year);
	
//	
//	SELECT calulation_rate FROM tbl_composite_tax WHERE calculation_type ='M' AND year = '2018-04-01';
	
	@Query("SELECT c.CalulationRate FROM CompositeTax c WHERE c.year = ?1 and c.calulationType = ?2")
	String findCalculationRateByTypeAndYear(@Param("year") String year, @Param("calulationType") String calulationType);


	
	
}
