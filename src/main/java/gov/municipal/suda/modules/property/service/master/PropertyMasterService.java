package gov.municipal.suda.modules.property.service.master;

import gov.municipal.suda.modules.property.dto.PropertySEarchViewDTO;
import gov.municipal.suda.modules.property.dto.PropertySearchDTO;
import gov.municipal.suda.modules.property.dto.PropertyViewDTO;
import gov.municipal.suda.modules.property.model.master.OwnerDetailsBean;
import gov.municipal.suda.modules.property.model.master.PropertyDocMstrBean;
import gov.municipal.suda.modules.property.model.master.PropertyMasterBean;
import gov.municipal.suda.modules.property.model.transaction.SAFViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PropertyMasterService {
   PropertyMasterBean propertyMasterEntry(PropertyMasterBean propertyMasterBean);

   List<Object[]> getAllProperty(Long ward_id, String owner_name, String property_no);

   List<PropertyDocMstrBean> fetchAllDocumentName();

    Optional<SAFViewDTO> getPropertyByPropNo(String property_no);

    List<PropertySearchDTO> getPropertyDetailsByPropId(Long propId);
    Page<PropertySEarchViewDTO> getPropDetailsByPropId(Long ward_id, String owner_name, String property_no, Pageable pageable);
    
    public Long generateProperty_No(long ward_id,long zone_id);
}
