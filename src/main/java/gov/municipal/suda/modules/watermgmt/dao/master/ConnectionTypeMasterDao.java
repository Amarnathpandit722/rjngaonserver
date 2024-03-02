package gov.municipal.suda.modules.watermgmt.dao.master;

import gov.municipal.suda.modules.watermgmt.model.master.ConnectionTypeMasterBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConnectionTypeMasterDao extends JpaRepository<ConnectionTypeMasterBean, Long> {
    @Query("select c from ConnectionTypeMasterBean c where c.conn_type=?1")
    ConnectionTypeMasterBean getConnectionTypeIdByConnectionType(String connectionType);
}
