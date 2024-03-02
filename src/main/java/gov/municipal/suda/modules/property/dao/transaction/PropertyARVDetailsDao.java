package gov.municipal.suda.modules.property.dao.transaction;

import gov.municipal.suda.modules.property.model.transaction.PropertyARVDetailsBean;
import gov.municipal.suda.modules.property.model.transaction.SAFARVDetailsBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PropertyARVDetailsDao extends JpaRepository<PropertyARVDetailsBean, Long> {

   @Query("select a from PropertyARVDetailsBean a where a.prop_id=?1 and a.status=1")
    Optional<List<PropertyARVDetailsBean>> getPropARVDetailsByPropIdAndStatusActive(Long prop_id);
   @Query("select p from PropertyARVDetailsBean p where p.prop_id=?1 and p.floor_name=?2 order by fy_end_date desc")
   List<PropertyARVDetailsBean> findArvDetailsByPropertyAndFloorName(Long prop_id,String floor_name);
   
   

}
