package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.model.master.ZoneBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ZoneDao extends JpaRepository<ZoneBean, Long> {

}
