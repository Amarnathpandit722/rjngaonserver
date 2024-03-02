package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.model.master.PropertyGeoLocationBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PropertyGeoLocationDao extends JpaRepository<PropertyGeoLocationBean, Integer> {
    @Query("select p from PropertyGeoLocationBean p where p.property_id=?1")
    List<PropertyGeoLocationBean> findPropertyGeoLocationByPropId(Long prop_id);
}
