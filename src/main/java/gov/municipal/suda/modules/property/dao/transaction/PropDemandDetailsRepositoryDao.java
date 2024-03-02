package gov.municipal.suda.modules.property.dao.transaction;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gov.municipal.suda.modules.property.dto.PropDemandDetailsDTO;
import gov.municipal.suda.modules.property.model.transaction.DemandDetailsBean;

@Repository
public interface PropDemandDetailsRepositoryDao extends JpaRepository<DemandDetailsBean, Long>{
//
//	@Query("SELECT " +
//            "SUM(CASE WHEN pdd.paid_status = 0 THEN pdd.total_amount ELSE 0 END) AS total_arrear_amount_sum, " +
//            "SUM(CASE WHEN pdd.paid_status = 0 THEN pdd.common_wtr_tax ELSE 0 END) AS arrear_common_wtr_tax_sum, " +
//            "SUM(CASE WHEN pdd.paid_status = 0 THEN pdd.education_cess ELSE 0 END) AS arrear_education_cess_sum, " +
//            "SUM(CASE WHEN pdd.paid_status = 1 THEN pdd.total_amount ELSE 0 END) AS total_current_amount_sum, " +
//            "SUM(CASE WHEN pdd.paid_status = 1 THEN pdd.common_wtr_tax ELSE 0 END) AS current_common_wtr_tax_sum, " +
//            "SUM(CASE WHEN pdd.paid_status = 1 THEN pdd.education_cess ELSE 0 END) AS current_education_cess_sum " +
//            "FROM DemandDetailsBean pdd " +
//            "WHERE pdd.demand_date BETWEEN :demand_date AND :demand_date")
	
	
    @Query("SELECT " +
            "SUM(CASE WHEN pdd.paid_status = 0 THEN pdd.total_amount ELSE 0 END) AS totalArrearAmountSum, " +
            "SUM(CASE WHEN pdd.paid_status = 0 THEN pdd.common_wtr_tax ELSE 0 END) AS arrearCommonWtrTaxSum, " +
            "SUM(CASE WHEN pdd.paid_status = 0 THEN pdd.education_cess ELSE 0 END) AS arrearEducationCessSum, " +
            "SUM(CASE WHEN pdd.paid_status = 1 THEN pdd.total_amount ELSE 0 END) AS totalCurrentAmountSum, " +
            "SUM(CASE WHEN pdd.paid_status = 1 THEN pdd.common_wtr_tax ELSE 0 END) AS currentCommonWtrTaxSum, " +
            "SUM(CASE WHEN pdd.paid_status = 1 THEN pdd.education_cess ELSE 0 END) AS currentEducationCessSum, " +
            "SUM(CASE WHEN pdd.paid_status = 0 THEN pdd.penal_charge ELSE 0 END) AS PenalCharge, " +
            "SUM(CASE WHEN pdd.paid_status = 1 THEN pdd.penal_charge ELSE 0 END) AS PenalCharge, " +
            "SUM(CASE WHEN pdd.paid_status = 0 THEN pdd.penalty ELSE 0 END) AS Penalty, " +
            "SUM(CASE WHEN pdd.paid_status = 1 THEN pdd.penalty ELSE 0 END) AS Penalty " +
            
            
            "FROM DemandDetailsBean pdd " +
            "WHERE pdd.demand_date BETWEEN :startDate AND :endDate")
    List<Object[]> getArrearAndCurrentAmount(@Param("startDate") String startDate, @Param("endDate") String endDate);

	
}
