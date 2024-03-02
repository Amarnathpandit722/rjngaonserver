package gov.municipal.suda.modules.watermgmt.dao.transaction;


import gov.municipal.suda.modules.watermgmt.model.transaction.WaterLastPaymentRecordBean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaterLastPaymentRecordDao extends JpaRepository<WaterLastPaymentRecordBean, Long> {
}
