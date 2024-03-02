package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.model.master.LastPaymentRecordBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface LastPaymentRecordDao extends JpaRepository<LastPaymentRecordBean, Long> {

    @Query("select l from LastPaymentRecordBean l where l.prop_id=?1 order by l.upto_year desc")
    Optional<List<LastPaymentRecordBean>> getLastPaymentIdByPropId(Long prop_id);

    @Query("select l from LastPaymentRecordBean l where l.prop_id=?1  order by l.upto_year desc")
    Optional<List<LastPaymentRecordBean>> getLastPaymentBetweenTheDate(Long prop_id);

    @Query("select l from LastPaymentRecordBean l where l.prop_id=?1")
    Optional<LastPaymentRecordBean> getPaymentDetailsByPropId(Long prop_id);

    @Query("select l from LastPaymentRecordBean l where l.prop_id=?1")
    Optional<List<LastPaymentRecordBean>> getPaymentByPropId(Long prop_id);
    @Query("select  max(p.id)+1 from LastPaymentRecordBean p")
    Long generateLastPaymentId();
    @Query("select l from LastPaymentRecordBean l where l.prop_id=?1 order by l.upto_year desc")
    List<LastPaymentRecordBean> getLastPaymentIdByPropId1(Long prop_id);
    @Modifying
    @Transactional
    @Query("delete from LastPaymentRecordBean l where l.id=?1")
    void deleteLastPaymentRecord(Long id);

}
