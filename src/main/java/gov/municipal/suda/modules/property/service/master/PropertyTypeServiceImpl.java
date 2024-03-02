package gov.municipal.suda.modules.property.service.master;

import gov.municipal.suda.modules.property.dao.master.PropertyTypeDao;
import gov.municipal.suda.modules.property.model.master.PropertyTypeBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PropertyTypeServiceImpl implements PropertyTypeService{
    @Autowired
    private PropertyTypeDao propertyTypeDao;
    @Override
    public Optional<List<PropertyTypeBean>> findAll() {
        return Optional.of(propertyTypeDao.findAll());
    }
}
