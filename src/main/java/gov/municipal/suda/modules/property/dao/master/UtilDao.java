package gov.municipal.suda.modules.property.dao.master;


import gov.municipal.suda.modules.property.dto.YearCountDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UtilDao extends CrudRepository<YearCountDTO, Long> {
    @Query(value = "SELECT DATE_PART('year', AGE(?1, ?2)) AS years ",nativeQuery = true)
    Long countNumberOfYear(String start_date, String end_date);
}
