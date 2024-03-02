package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.model.master.PropertyIdAndPropertyNoRelationBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PropertyIdAndPropertyNoRelationDao extends JpaRepository<PropertyIdAndPropertyNoRelationBean, Long> {

    @Query("select count(*)from PropertyIdAndPropertyNoRelationBean r where r.prop_id=?1")
    public Long findRecordCountByPropertyId(Long prop_id);

    @Query("select r from PropertyIdAndPropertyNoRelationBean r where r.prop_id=?1")
    public PropertyIdAndPropertyNoRelationBean findByProperty_id(Long prop_id);
}
