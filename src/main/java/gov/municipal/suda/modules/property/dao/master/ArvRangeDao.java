package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.dto.ARVRangePercentageDTO;
import gov.municipal.suda.modules.property.model.master.ArvRangeBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ArvRangeDao extends JpaRepository<ArvRangeBean, Long> {
@Query("select NEW gov.municipal.suda.modules.property.dto.ArvRangeDTO(a.id,concat(a.range_from,' TO ', a.range_to, ' => ', a.effect_date )) from ArvRangeBean a order by a.effect_date desc")
    List<Object> getAllRange();
   // Double findArvPercentageByRangeAndDate(Long rangeFrom, Long rangeTo, LocalDate effect_date);

    @Query("select  r from ArvRangeBean r where ?1 between r.range_from and r.range_to and r.effect_date=?2 order by r.effect_date asc")
    Optional<List<ArvRangeBean>> getArvRangePercentageByRangeAndYear(Long finalArvValue,String doe);

    @Query("select  r from ArvRangeBean r where r.effect_date=?1 order by r.effect_date asc")
    Optional<List<ArvRangeBean>> getMaxArvRangePercentageForComplex(String doe);


    @Query("select  r from ArvRangeBean r where ?1 between r.range_from and r.range_to  order by r.effect_date desc")
    Optional<List<ArvRangeBean>> getArvRangePercentageByRange(Long finalArvValue);
    @Query("select distinct r.effect_date from ArvRangeBean r where r.effect_date between ?1 and ?2")
    List<ArvRangeBean> getEffectDateByFisicalYear(String fromDate, String toDate);

    @Query("select r.effect_date from ArvRangeBean r order by effect_date desc")
    List<ArvRangeBean> findRecentEffectDate();
    
    
//    @Query("SELECT a.percentage FROM ArvRangeBean a WHERE :number BETWEEN a.range_from AND a.range_to AND a.effect_date = :effect_date")
    
    
    @Query("SELECT a.percentage FROM ArvRangeBean a WHERE :number >= a.range_from AND :number <= a.range_to AND a.effect_date = :effect_date")
    BigDecimal findPercentageByNumberAndDate(@Param("number") long number, @Param("effect_date") String effect_date);

    @Query("SELECT a FROM ArvRangeBean a WHERE :number >= a.range_from AND :number <= a.range_to AND a.effect_date = :effect_date")
    ArvRangeBean findArvRange(@Param("number") long number, @Param("effect_date") String effect_date);
}
