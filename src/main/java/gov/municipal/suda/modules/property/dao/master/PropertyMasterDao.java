package gov.municipal.suda.modules.property.dao.master;


import gov.municipal.suda.modules.property.model.master.PropertyMasterBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PropertyMasterDao  extends JpaRepository<PropertyMasterBean, Long> {

    @Query("select p.property_no from PropertyMasterBean p where p.property_no=?1")
    Optional<String> findPropertyNo(String propertyNo);

    @Query("select p.id from PropertyMasterBean p where p.property_no=?1")
    Long findIdByPropNo(String propertyNo);
    @Query("select p from PropertyMasterBean p where p.property_no=?1")
    PropertyMasterBean findPropertyByPropNo(String propertyNo);
    @Query("select p from PropertyMasterBean p where p.id=?1")
    List<PropertyMasterBean> fetchAllPropById(Long id);

    @Query("select  m.entry_fy_id from PropertyMasterBean m where m.id=?1 and m.entry_type = 'Re Assessment' ")
    Long findEntryFyIDByIDAndEntryType(Long id);
    
    // generate Property No by New Implementation
    @Query("select max(pmb.id)+1 from PropertyMasterBean pmb ")
    Long generatePropertyNo();
    
    
}
