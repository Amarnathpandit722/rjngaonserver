package gov.municipal.suda.modules.property.dao.transaction;

import gov.municipal.suda.modules.property.dto.PropertySEarchViewDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertySearchViewDao extends JpaRepository<PropertySEarchViewDTO, Long> {
}
