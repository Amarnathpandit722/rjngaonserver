package gov.municipal.suda.modules.watermgmt.dao.master;

import gov.municipal.suda.modules.watermgmt.model.master.PropertyTypeWaterMasterBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PropertyTypeWaterDao extends JpaRepository<PropertyTypeWaterMasterBean, Long> {
    @Query("select t from PropertyTypeWaterMasterBean t where t.prop_type=?1 and t.status=1")
    PropertyTypeWaterMasterBean getWaterPropertyTypeIdByPropType(String prop_type);
}
