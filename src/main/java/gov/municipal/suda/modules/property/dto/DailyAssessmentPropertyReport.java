package gov.municipal.suda.modules.property.dto;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gov.municipal.suda.modules.property.model.master.PropertyMasterBean;

@Repository
public interface DailyAssessmentPropertyReport extends JpaRepository<PropertyMasterBean, Long>{

	
	@Query("SELECT DISTINCT " +
	        "tpm.ward_id, " +
	        "tpm.entry_type, " +
	        "tpm.property_no, " +
	        "tpm.property_address, " +
	        "tpm.entry_date, " +
	        "tpod.owner_name, " +
	        "tpm.user_id, " +
	        "SUM(tdd.total_amount) AS total_amount_sum, " +
	        "SUM(tdd.common_wtr_tax) AS common_wtr_tax_sum, " +
	        "SUM(tdd.education_cess) AS education_cess_sum, " +
	        "us.userName " +
	        "FROM PropertyMasterBean tpm " +
	        "JOIN OwnerDetailsBean tpod ON tpm.id = tpod.prop_id " +
	        "JOIN DemandDetailsBean tdd ON tpm.id = tdd.prop_id " +
	        "JOIN User us ON tpm.user_id = us.user_id " +
	        "WHERE tpm.entry_date BETWEEN :startDate AND :endDate " +
	        "GROUP BY " +
	        "tpm.ward_id, " +
	        "tpm.entry_type, " +
	        "tpm.property_no, " +
	        "tpm.property_address, " +
	        "tpm.entry_date, " +
	        "tpod.owner_name, " +
	        "tpm.user_id, " +
	        "us.userName")
	
	List<Object[]> getSummaryDataRepo( @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate );
	
	
}
