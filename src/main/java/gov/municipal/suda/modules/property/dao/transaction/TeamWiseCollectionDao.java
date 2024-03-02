package gov.municipal.suda.modules.property.dao.transaction;

import gov.municipal.suda.modules.property.dto.TeamWiseCollectionDTO;
import gov.municipal.suda.modules.property.model.transaction.DemandDetailsBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface TeamWiseCollectionDao extends JpaRepository<TeamWiseCollectionDTO, Long> {

    @Query(value = "select d.collector_name,d.user_type,sum(d.total_collection)as total_collection ,count(d.property_count) as property_count  from TeamWiseCollectionDTO d where d.stampdate between ?1 and ?2 and d.user_id=?3 GROUP BY collector_name,user_type")
    List<String> teamWiseCollectionByUser(Timestamp dateFrom, Timestamp dateTo, BigInteger userId);

}
