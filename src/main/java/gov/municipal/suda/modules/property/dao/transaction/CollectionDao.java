package gov.municipal.suda.modules.property.dao.transaction;

import gov.municipal.suda.modules.property.model.transaction.PropertyCollectionMstrBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public interface CollectionDao extends JpaRepository<PropertyCollectionMstrBean, Long> {

    @Query("select sum(r.tot_amount) from PropertyCollectionMstrBean r where r.stampdate between ?1 and ?2 and r.status=1")
    BigDecimal findByDateBetween(Timestamp firstDayOfYear, Timestamp lastDayOfYear);

    @Query("select c from PropertyCollectionMstrBean c where c.prop_id=?1 and c.transaction_id=?2 and c.status=1")
    List<PropertyCollectionMstrBean> findCollectionByPropIdAndTransactionId(Long propId, Long tranId);
    
    
    
    
    


}
