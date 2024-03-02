package gov.municipal.suda.modules.wastemgmt.dao.transaction;

import gov.municipal.suda.modules.wastemgmt.model.transaction.ConsumerDemandBean;
import gov.municipal.suda.modules.wastemgmt.model.transaction.ConsumerTransactionBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ConsumerDemandDao extends JpaRepository<ConsumerDemandBean,Long> {
    @Query("select  p from ConsumerDemandBean p where p.consumer_detail_id=?1 and p.payment_status=0 and p.status=1 order by p.demand_from asc, p.demand_amount")
    List<ConsumerDemandBean> fetchConsumerDueDemandByDtlId(Long consumer_detail_id);
    @Query("select  p from ConsumerDemandBean p where id=?1 and payment_status=0 and p.status=1")
    List<ConsumerDemandBean> fetchDemandById(Long id);
    @Query("select d from ConsumerDemandBean d where d.consumer_detail_id=?1 and d.demand_from >=?2 and d.demand_to <= ?3 and d.payment_status=0 and d.status=1")
    List<ConsumerDemandBean> getDemandBetweenFromAndToDateByConsumerId(Long consumerDetailsId, String demandFrom, String demandUpTo);
    @Query("select d from ConsumerDemandBean d where  d.consumer_detail_id=?1 and TO_CHAR(CAST(d.demand_to AS date),'YYYY-MM') <= ?2 and d.payment_status=0 and d.status=1")
    List<ConsumerDemandBean> getDemandBetweenFromAndToDateByConsumerId(Long consumerDetailsId, String demandUpTo);
    @Query("select distinct d.rate_chart_id from ConsumerDemandBean d where d.consumer_detail_id=?1 and d.status=1")
    List<Long> getDistinctRateChartId(Long consumerDetailsId);

    //and p.rate_chart_id=?2
    @Query("select  p from ConsumerDemandBean p where p.consumer_detail_id=?1 and p.rate_chart_id=?2 and p.payment_status=0 and p.status=1")
    List<ConsumerDemandBean> fetchConsumerDueDemandByDtlIdAndRateChartId(Long consumer_detail_id, Long prev_chart_id);

    @Query("select  p from ConsumerDemandBean p where p.consumer_detail_id=?1 and p.status=1 order by p.demand_to desc")
    List<ConsumerDemandBean> fetchGeneratedDemandByConsumerDetailsId(Long consumer_detail_id);

}
