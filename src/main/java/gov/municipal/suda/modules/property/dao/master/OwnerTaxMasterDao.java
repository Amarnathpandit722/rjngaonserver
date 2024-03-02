package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.model.master.OwnerTaxMasterBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OwnerTaxMasterDao extends JpaRepository<OwnerTaxMasterBean, Long> {

    @Query("select p from OwnerTaxMasterBean p where p.prop_id=?1 and p.status=1 order by p.effect_year ASC")
    List<OwnerTaxMasterBean> getPropertyOwnerTaxByPropId(Long prop_id);

    @Modifying
    @Query(value = "update OwnerTaxMasterBean p SET p.status = 0 where p.prop_id=?1 and p.status=1")
    void updatePropertyOwnerTaxByPropId(@Param("prop_id")Long prop_id);

    @Query("select p from OwnerTaxMasterBean p where p.prop_id=?1 and p.status=1")
    Optional<List<OwnerTaxMasterBean>> getTaxByPropertyIdAndStatus(Long prop_id);
    @Query("select p from OwnerTaxMasterBean p where p.prop_id=?1 and p.status=1 order by p.arv desc")
    Optional<List<OwnerTaxMasterBean>> getTaxByProp_id(Long prop_id);

}
