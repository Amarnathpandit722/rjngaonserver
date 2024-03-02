package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.model.master.WardBean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@Repository
public interface WardDao extends JpaRepository<WardBean,Long> {
    @Query("select w.ward_name from WardBean w where w.ward_name=?1")
    Optional<String> findWardName(String ward_name);

    @Query("select w from WardBean w where w.ward_name=?1")
    Optional<WardBean> findZoneIdByWardName(String ward_name);

    @Query("select w.ward_name from WardBean w where w.id=?1")
    String findWardNameById(Long ward_id);

}
