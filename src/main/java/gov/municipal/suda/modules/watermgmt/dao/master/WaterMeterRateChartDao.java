package gov.municipal.suda.modules.watermgmt.dao.master;


import gov.municipal.suda.modules.watermgmt.model.master.WaterMeterRateChartBean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaterMeterRateChartDao extends JpaRepository<WaterMeterRateChartBean, Long> {
}
