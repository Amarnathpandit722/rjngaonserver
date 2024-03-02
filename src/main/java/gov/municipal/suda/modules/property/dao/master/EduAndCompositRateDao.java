package gov.municipal.suda.modules.property.dao.master;
import gov.municipal.suda.modules.property.model.master.EduAndCompositeRateBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EduAndCompositRateDao extends JpaRepository<EduAndCompositeRateBean, Long> {
   @Query("select e from EduAndCompositeRateBean e where e.doe=?1")
    EduAndCompositeRateBean getEduAndCompCessRate(String doe);
    @Query("select count(*) from EduAndCompositeRateBean e where e.doe=?1")
    Integer checkEffectDateByEffectDate(String effect_date);

    @Query("select e from EduAndCompositeRateBean e where e.doe between ?1 and ?2 order by e.doe desc ")
    List<EduAndCompositeRateBean> findEduAndCessInBetweenTheDate(String from_year, String to_year);

    @Query("select  r from EduAndCompositeRateBean r  order by r.doe DESC")
    List<EduAndCompositeRateBean> findMaxDoeDate(Pageable pageable);


}
