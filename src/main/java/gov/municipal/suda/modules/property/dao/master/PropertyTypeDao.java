package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.model.master.PropertyTypeBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PropertyTypeDao extends JpaRepository<PropertyTypeBean, Long> {
    @Query("select w.property_type_name from PropertyTypeBean w where w.id=?1")
    String findPropertTypeNameById(Long id);
}
