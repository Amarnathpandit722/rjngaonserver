package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.model.master.UsesTypeBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsesTypeDao extends JpaRepository<UsesTypeBean, Long> {
    @Query("select w.uses_type_name from UsesTypeBean w where w.id=?1")
    String findUsesTypeNameById(Long id);
}
