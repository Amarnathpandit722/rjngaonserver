package gov.municipal.suda.modules.watermgmt.dao.transaction;

import gov.municipal.suda.modules.watermgmt.model.transaction.DemandDetailsWaterBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DemandDetailsWaterDao extends JpaRepository<DemandDetailsWaterBean, Long> {
    @Query("select d from DemandDetailsWaterBean d where d.consumer_dets_id=?1 and d.payment_status=1 and d.status=1")
    Optional<DemandDetailsWaterBean> getDemandByPaymentStatusDuringNewEntry(Long consumerDetailsId);
    @Query("select d from DemandDetailsWaterBean d where d.consumer_dets_id=?1 and d.payment_status=0 and d.status=1")
    List<DemandDetailsWaterBean> getDueDemandByConsumerId(Long consumerDetailsId);
    @Query("select d from DemandDetailsWaterBean d where d.consumer_dets_id=?1  and d.status=1")
    List<DemandDetailsWaterBean> getDemandByConsumerId(Long consumerDetailsId);
    @Query("select d from DemandDetailsWaterBean d where d.consumer_dets_id=?1 and d.demand_upto=?2  and d.status=1")
    DemandDetailsWaterBean getDemandByConsumerIdAndDemandUpTo(Long consumerDetailsId,String demandUpTo);

    @Query("select d from DemandDetailsWaterBean d where d.consumer_dets_id=?1  and d.payment_status=0 and d.status=1 order by to_date(d.demand_upto,'DD/MM/YYYY') desc")
    List<DemandDetailsWaterBean> getLastPaymentDay(Long consumerDetailsId);

    @Query("select d from DemandDetailsWaterBean d where d.consumer_dets_id=?1 and d.demand_upto=?2 and d.status=1 and d.payment_status=1 and d.status=1")
    DemandDetailsWaterBean checkDemandGenerate(Long consumerDetailsId,String upToDate);
//    @Query("select cast(max(to_date(d.demand_upto,'DD/MM/YYYY')) AS text) from DemandDetailsWaterBean d where d.consumer_dets_id=?1")
//    DemandDetailsWaterBean findLatestDemandUpToByConsumerDetails(Long consumerDetailsId);

//    SET datestyle = dmy; // for run below query need to execute this query on DB first
    @Query("select d from DemandDetailsWaterBean d where d.consumer_dets_id=?1 and d.demand_upto between ?2 and ?3  and d.payment_status=0 and d.status=1")
    List<DemandDetailsWaterBean> getAllDueDemandByConsumerIdsAndDateRange(Long consumerDetailsId, String fromDate, String to_date);

    @Query("select d from DemandDetailsWaterBean d where d.consumer_dets_id=?1 and TO_CHAR(TO_DATE(d.demand_upto,'DD-MM-YYYY'),'YYYY-MM') between ?2 and ?3 and d.payment_status=0 and d.status=1")
    List<DemandDetailsWaterBean> getDueDemandByConsumerIdsTillUpToDate(Long consumerIds, String demandFrom, String demandUpToDate);

}
