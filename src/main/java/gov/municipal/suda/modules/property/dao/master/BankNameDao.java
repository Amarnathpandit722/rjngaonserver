package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.model.master.BankNameBean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankNameDao extends JpaRepository<BankNameBean,Long> {
}
