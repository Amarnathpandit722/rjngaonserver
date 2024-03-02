package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.dto.ArvRateDoeDTO;
import gov.municipal.suda.modules.property.model.master.ArvRateBean;
import gov.municipal.suda.util.enumtype.FloorType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ArvRateDao extends JpaRepository<ArvRateBean, Long> {
   // @Query("select m from ArvRateBean m where m.zone_id=?1 and m.road_id=?2 and m.const_type=?3 and m.usestype_id=?4 and date_part('year',To_date(m.doe,'YYYY-MM-DD'))= date_part('year',To_date(?5,'YYYY'))")
   @Query("select m from ArvRateBean m where m.zone_id=?1 and m.road_id=?2 and m.const_type=?3 and m.usestype_id=?4 and m.doe between ?5 and ?6 order by m.doe asc")
    Optional<List<ArvRateBean>> findArvRateByUsesTypeAndFYear(Long zone_id, Long road_id, String const_type, Long uses_type_id, String from_year, String to_date); // doe should get from tbl_ratechange_effect (effect_date) field
 @Query("select m from ArvRateBean m where m.zone_id=?1 and m.road_id=?2 and m.const_type=?3 and m.usestype_id=?4 and m.doe=?5 order by m.doe asc")
 Optional<List<ArvRateBean>> findArvRateByTypes(Long zone_id, Long road_id, String const_type, Long uses_type_id,String doe);

//    @Query("select m from ArvRateBean m where m.zone_id=?1 and m.doe=?2 and m.const_type = ?3 and m.road_id=?4")
//    Optional<List<ArvRateBean>> findArvRateForIsComplex(Long zone_id,String doe,String const_type, Long road_id );
    
    
    
 @Query("select m from ArvRateBean m where m.zone_id=?1 and m.doe=?2 and m.const_type = ?3 and m.road_id = ?4")
 ArvRateBean findArvRateForIsComplex(Long zone_id, String doe, String const_type, Long road_id);
    
    
 @Query("select m from ArvRateBean m where m.zone_id=?1 and m.doe=?2 and m.const_type = ?3 and m.road_id = ?4 and m.floor_type= ?5")
 ArvRateBean findArvRateForIsComplex(Long zone_id, String doe, String const_type, Long road_id,FloorType floor_type);
    

 @Query("select m from ArvRateBean m order by m.doe asc")
    List<ArvRateBean> getAllDOEByAscendingOrder();
    @Query("select distinct new gov.municipal.suda.modules.property.dto.ArvRateDoeDTO(a.doe) from ArvRateBean a order by a.doe asc")
    List<ArvRateDoeDTO> findAllDoeByAscendingOrder();

    @Query("select distinct new gov.municipal.suda.modules.property.dto.ArvRateDoeDTO(a.doe) from ArvRateBean a order by a.doe desc")
    List<ArvRateDoeDTO> findAllDoeByDescendingOrder();

    @Query("select distinct new gov.municipal.suda.modules.property.dto.ArvRateDoeDTO(r.doe) from ArvRateBean r where r.doe between '2000' and '2017' order by doe asc")
    List<ArvRateDoeDTO> findAllDOEByDateRange(String from_year, String to_year);

//    @Query("select distinct new gov.municipal.suda.modules.property.dto.ArvRateEffectiveDateDTO(m.doe) from ArvRateBean m where m.status=1")
//    List<ArvRateEffectiveDateDTO> findEffectiveDate();

    @Query("select m.building_rate from ArvRateBean m where m.zone_id=?1 AND m.const_type = ?2 AND m.doe = ?3  AND m.road_id= ?4 AND m.status =1") 
    BigDecimal findByZoneConstType(Long zone_id, String const_type, String doe , Long road_id);

    
    
}
