package gov.municipal.suda.modules.property.dao.transaction;

import gov.municipal.suda.modules.property.model.transaction.ARVDetailsBean;
import gov.municipal.suda.modules.property.model.transaction.PropertyARVDetailsBean;
import gov.municipal.suda.modules.property.model.transaction.SAFARVDetailsBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SAFARVDetailsDao extends JpaRepository<SAFARVDetailsBean, Long> {
    @Query("select max(substring(code,5,1)) as code from SAFARVDetailsBean where prop_id=?1")
    Integer findMaxCodePerPropId(Long prop_id); // this is for RE-Assessment time

    @Query("select p from SAFARVDetailsBean p where p.prop_id=?1 AND p.status=1")
    List<SAFARVDetailsBean> findSAFARVDetails(Long prop_id);

    @Query("select p from SAFARVDetailsBean p where p.prop_id=?1")
    Optional<SAFARVDetailsBean> findSAFARVDetailsForUpdate(Long prop_id);

    @Query("select d from SAFARVDetailsBean d where d.prop_id=?1 and d.effect_year between ?2 and ?3 order by d.stampdate desc")
    Optional<List<SAFARVDetailsBean>> getArvDetailsByPropIdAndEffectYear(Long prop_id,String from_year, String to_year);
    @Modifying
    @Query(value = "update SAFARVDetailsBean p SET p.status = 0 where p.prop_id=?1 and p.status=1")
    void updateFloorDetailsStatus(@Param("prop_id")Long prop_id);

    @Query("select a from SAFARVDetailsBean a where a.prop_id=?1 and a.status=1")
    Optional<List<SAFARVDetailsBean>> getSAFARVDetailsByPropIdAndStatusActive(Long prop_id);

    @Query("select d from SAFARVDetailsBean d where d.prop_id=?1 and d.effect_year =?2 and d.status=1 order by d.stampdate desc")
    Optional<List<SAFARVDetailsBean>> getSafArvByPropAndYear(Long prop_id, String from_year);


    
    
    
    

}
