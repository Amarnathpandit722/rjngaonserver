package gov.municipal.suda.modules.property.dao.transaction;

import gov.municipal.suda.modules.property.model.master.PropertyMasterBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SAFDao extends JpaRepository<PropertyMasterBean, Long> {
  //List<PropertyMasterBean> findPropertyByPropertyNo();
  @Query("select s from PropertyMasterBean s")
    List<PropertyMasterBean> findAllByPropertyNo(Pageable page);
    //List<Product> findAllByPrice(double price, Pageable pageable);
}
