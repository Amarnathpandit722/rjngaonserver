package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.model.master.FinYearBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FinYearDao extends JpaRepository<FinYearBean, Long> {
    @Query("select f from FinYearBean f where f.fy_name between ?1 and ?2 ")
    Optional<List<FinYearBean>> countFinancialYear(String from, String to);
    @Query("select f from FinYearBean f where f.fy_name=?1")
    FinYearBean getFinIdByFinYear(String fin_year);

    @Query("select f from FinYearBean f order by f.id asc")
    List<FinYearBean> findAllFinancialYear();

    @Query("select w.fy_name from FinYearBean w where w.id=?1")
    String findYearNameById(Long id);

    @Query("select f from FinYearBean f where substring(f.fy_name,6,5)=?1 and status=1")
    FinYearBean findFinYearByExtractLastYear(String subStringYear);
}
