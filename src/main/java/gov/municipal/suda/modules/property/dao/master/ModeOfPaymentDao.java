package gov.municipal.suda.modules.property.dao.master;


import gov.municipal.suda.modules.property.model.master.ModeOfPaymentBean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModeOfPaymentDao extends JpaRepository<ModeOfPaymentBean, Long> {
}
