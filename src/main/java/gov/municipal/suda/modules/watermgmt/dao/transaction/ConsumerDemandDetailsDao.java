package gov.municipal.suda.modules.watermgmt.dao.transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gov.municipal.suda.modules.watermgmt.model.master.WaterConsumerDetailsBean;
import gov.municipal.suda.modules.watermgmt.model.transaction.DemandDetailsWaterBean;

@Repository
public interface ConsumerDemandDetailsDao extends JpaRepository<DemandDetailsWaterBean, Long>{

//	@Query("SELECT " +
//            "SUM(CASE WHEN tcdd.payment_status = 0 THEN tcdd.demand_amount ELSE 0 END) AS totalWaterArrearAmount, " +
//            "SUM(CASE WHEN tcdd.payment_status = 1 THEN tcdd.demand_amount ELSE 0 END) AS totalWaterCurrentAmount, " +      
//            "FROM gov.municipal.suda.modules.watermgmt.model.master.WaterConsumerDetailsBean tcdd" +
//            "WHERE tcdd.demand_date BETWEEN :startDate AND :endDate")
//    List<Object[]> getArrearAndCurrentAmount(@Param("startDate") String startDate, @Param("endDate") String endDate);

	
	@Query("SELECT " +
	        "SUM(CASE WHEN tcdd.payment_status = 0 THEN tcdd.demand_amount ELSE 0 END) AS totalWaterArrearAmount, " +
	        "SUM(CASE WHEN tcdd.payment_status = 1 THEN tcdd.demand_amount ELSE 0 END) AS totalWaterCurrentAmount " +
	        "FROM DemandDetailsWaterBean tcdd " +
	        "WHERE tcdd.demand_date BETWEEN :startDate AND :endDate")
	List<Object[]> getArrearAndCurrentAmount(@Param("startDate") String startDate, @Param("endDate") String endDate);

//	@Query("SELECT " +
//	        "SUM(CASE WHEN tcdd.paymentStatus = 0 THEN tcdd.demandAmount ELSE 0 END) AS totalWaterArrearAmount, " +
//	        "SUM(CASE WHEN tcdd.paymentStatus = 1 THEN tcdd.demandAmount ELSE 0 END) AS totalWaterCurrentAmount " +
//	        "FROM ConsumerDemandDetails tcdd " +
//	        "WHERE tcdd.demandDate BETWEEN :startDate AND :endDate")
//	List<Object[]> getArrearAndCurrentAmount(@Param("startDate") String startDate, @Param("endDate") String endDate);
//
//
//	
	
	
	
	
}
