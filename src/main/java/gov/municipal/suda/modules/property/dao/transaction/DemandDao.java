package gov.municipal.suda.modules.property.dao.transaction;

import gov.municipal.suda.modules.property.model.transaction.DemandDetailsBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DemandDao extends JpaRepository<DemandDetailsBean, Long> {
    @Query("select p from DemandDetailsBean p where p.prop_id=?1 and p.status=1 and p.paid_status=0 and p.demand_deactive=0 order by p.effect_year ASC")
    List<DemandDetailsBean> getDemandDetailsByPropId(Long prop_id);
    @Query("select p from DemandDetailsBean p where p.prop_id=?1 and p.status=1 and p.paid_status=0 and p.demand_deactive=0 and p.effect_year=?2 order by p.effect_year ASC")
    List<DemandDetailsBean> getDemandDetailsByPropId1(Long prop_id,String effect_year);

    @Query("select p.last_payment_id from DemandDetailsBean p where p.prop_id=?1")
    Long getLastPaymentId(Long prop_id);

    @Query("select max(CAST(d.demand_no AS int))+1 from DemandDetailsBean d")
    Integer generateDemandNo();

    @Query("select count(d.demand_no) from DemandDetailsBean d where d.demand_no=?1")
    Integer  checkDuplicateDemandNo(String demandNo);
    @Query("select count(*) from DemandDetailsBean d where d.prop_id=?1 and d.effect_year=?2 and d.paid_status=0 and d.demand_deactive=0")
    Integer getDemandByPropIdAndEffectYear(Long prop_id, String effect_year);

    @Query("select t from DemandDetailsBean t where t.prop_id=?1 and t.effect_year=?2 and t.paid_status=0 and t.demand_deactive=0")
    Optional<DemandDetailsBean> getPayableDemand(Long prop_id, String effective_year);

    @Query("select p from DemandDetailsBean p where p.prop_id=?1 and p.effect_year=?2 and p.status=1 and p.paid_status=1 and p.demand_deactive=0")
    Optional<List<DemandDetailsBean>> getPaidDemand(Long prop_id, String effect_year);

    @Query("select p from DemandDetailsBean p where p.prop_id=?1 and p.effect_year=?2 and p.paid_status=1 and p.demand_deactive=0")
    Optional<List<DemandDetailsBean>> getDemandForDefferenceAmount(Long prop_id,String currentEffectYear);
    @Query("select d from DemandDetailsBean d where d.prop_id=?1 and d.effect_year between ?2 and ?3 and d.paid_status=1 and d.demand_deactive=0 order by d.demand_date asc")
    Optional<List<DemandDetailsBean>> showPaidDemandDetailsForPaymentReceipt(Long prop_id,String from_year, String to_year);

    @Query("select d from DemandDetailsBean d where d.prop_id=?1 and d.effect_year between ?2 and ?3 and d.paid_status=1 and d.diff_sts=0 and d.demand_deactive=0 order by d.effect_year asc")
    Optional<List<DemandDetailsBean>> showPaidDemandDetailsForPaymentReceiptForAdjustmentAmount(Long prop_id,String from_year, String to_year);

    @Query("select count(*) from DemandDetailsBean d where d.prop_id=?1 and d.effect_year between ?2 and ?3 and d.paid_status=1 and d.diff_sts=1 and d.demand_deactive=0")
    Integer countDifferenceAmountDemandByPropId(Long prop_id,String from_year, String to_year);

    @Query("select d from DemandDetailsBean d where d.prop_id=?1 and d.effect_year between ?2 and ?3 and d.paid_status=1 and d.diff_sts=1 and d.demand_deactive=0 order by d.demand_date asc")
    Optional<List<DemandDetailsBean>> showPaidDemandDetailsForPaymentReceiptForDifferenceAmount(Long prop_id,String from_year, String to_year);

    @Query("select p from DemandDetailsBean p where p.prop_id=?1 and p.status=1 and p.paid_status=1 and p.demand_deactive=0")
    Optional<List<DemandDetailsBean>> getDemandByPropIdAndStatus(Long prop_id);

    @Query("select count(*) from DemandDetailsBean p where p.prop_id=?1 and p.status=1 and p.paid_status=1 and p.demand_deactive=0")
    Integer checkPaidDemandByPropertyId(Long prop_id);

    @Query("select p from DemandDetailsBean p where p.id=?1 and p.paid_status=0 and p.demand_deactive=0")
    Optional<DemandDetailsBean> findByDemandId(Long id);
    @Query("select p from DemandDetailsBean p where p.prop_id=?1 and p.diff_sts=1 and p.demand_deactive=0")
    Optional<List<DemandDetailsBean>> findDifferenceAmount(Long prop_id);
    @Modifying
    @Transactional
    @Query("UPDATE DemandDetailsBean e SET e.paid_status = 0 , e.last_payment_id =0 WHERE e.last_payment_id = ?1")
    void reopenDemandByPaymentId(Long last_payment_id);
    @Query("select d from DemandDetailsBean d where d.prop_id= ?1 and d.effect_year<=?2 and d.paid_status=0 and d.demand_deactive=0 and status=1")
    List<DemandDetailsBean> getDueDemandByPropIdAndEffectYear(Long prop_id, String effect_year);

    @Query("select d from DemandDetailsBean d where d.prop_id=?1 and d.id IN ?2 and d.paid_status=1 order by d.effect_year asc")
    List<DemandDetailsBean> findPaidDemandByPropIdAndDemandIds(Long propId,  Set<Long> demandIds);

    @Query("select d from DemandDetailsBean d where d.id=?1 and d.paid_status=1")
    DemandDetailsBean findOneRecordByActivePayment(Long demandId);
    
    @Query("select dn from DemandDetailsBean dn where dn.demand_no=:demand_no")
    List<DemandDetailsBean> findDemandByDemandNumber(String demand_no);
    
    @Query("select d from DemandDetailsBean d where d.prop_id= ?1 and d.effect_year<=?2 and d.paid_status=1 and d.demand_deactive=0 and status=1")
    List<DemandDetailsBean> getNewDueDemandByPropIdAndEffectYear(Long prop_id, String effect_year);
    
    @Query("select p from DemandDetailsBean p where p.prop_id=?1 and p.status=1 and p.paid_status=1 and p.demand_deactive=0 order by p.effect_year ASC")
    List<DemandDetailsBean> getNewDemandDetailsByPropId(Long prop_id);
    

}
