package gov.municipal.suda.modules.wastemgmt.dao.master;

import gov.municipal.suda.modules.wastemgmt.dto.RangeAndRateDto;
import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerRateChartBean;
import gov.municipal.suda.modules.wastemgmt.model.transaction.ConsumerRateDetailsBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;
@Repository
public interface ConsumerRateChartDao extends JpaRepository<ConsumerRateChartBean,Long> {
    @Query("select d from ConsumerRateChartBean d where d.consumer_range_mstr_id=?1")
    List<ConsumerRateChartBean> fetchMonthlyAmount(Long consumer_range_mstr_id);

    @Query("select d from ConsumerRateChartBean d where d.id=?1")
    List<ConsumerRateChartBean> fetchMonthlyAmountByRateId(Long rateId);

//GROUP BY r.amount
    @Query("SELECT DISTINCT new gov.municipal.suda.modules.wastemgmt.dto.RangeAndRateDto(r.consumer_range_mstr_id,r.range.consumer_cat_mstr_id,r.range.range_name,r.id,r.amount,r.fee_effectdate) FROM ConsumerRateChartBean r WHERE r.range.id in :consumerRangeMasterId ")
    Set<RangeAndRateDto> findRateByRangeId(@Param("consumerRangeMasterId") Set<Long> consumerRateChartBean);
    @Query("select d from ConsumerRateDetailsBean d where d.id in :rateIds")
    List<ConsumerRateChartBean> findConsumerRateByMultipleId(List<Long> rateIds);
    @Query("select d from ConsumerRateDetailsBean d where d.id=?1 and d.status=1")
    Optional<ConsumerRateChartBean> findByIdAndStatus(Long rateId);

}
