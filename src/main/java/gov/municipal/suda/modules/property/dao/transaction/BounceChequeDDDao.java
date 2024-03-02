package gov.municipal.suda.modules.property.dao.transaction;


import gov.municipal.suda.modules.property.model.transaction.CounterCollectionBouncedChequeDDView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BounceChequeDDDao  extends JpaRepository<CounterCollectionBouncedChequeDDView, Long> {
  @Query("select r from CounterCollectionBouncedChequeDDView r  where TO_CHAR(r.stampdate,'YYYY-MM-DD') between ?1 and ?2 and  r.ward_id=?3 and r.user_id=?4 ")
  List<CounterCollectionBouncedChequeDDView> bounceReport(String from_date, String to_date, Long ward_id, Long user_id);
}
