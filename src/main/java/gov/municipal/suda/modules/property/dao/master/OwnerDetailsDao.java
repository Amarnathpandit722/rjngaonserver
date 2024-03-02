package gov.municipal.suda.modules.property.dao.master;


import gov.municipal.suda.modules.property.model.master.OwnerDetailsBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OwnerDetailsDao extends JpaRepository<OwnerDetailsBean, Long> {
    @Query("select p from OwnerDetailsBean p where p.prop_id=?1")
    List<OwnerDetailsBean> findOwnerDetails(Long prop_id);
    @Query("select p from OwnerDetailsBean p where p.prop_id=?1")
    Optional<OwnerDetailsBean> findOwnerDetailsByPropId(Long prop_id);
    @Query("select p from OwnerDetailsBean p where p.prop_id=?1")
    Optional<OwnerDetailsBean> findOwnerDetailsForUpdate(Long propId);
    
    @Query("select gt.govTap_Conn from OwnerDetailsBean gt where gt.prop_id=?1")
    String findIsOwnerHaveGov_Tap(Long prop_id);
}
