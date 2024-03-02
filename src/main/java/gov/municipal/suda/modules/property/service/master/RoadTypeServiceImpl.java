package gov.municipal.suda.modules.property.service.master;

import gov.municipal.suda.modules.property.dao.master.RoadTypeDao;
import gov.municipal.suda.modules.property.model.master.RoadTypeBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@Slf4j
public class RoadTypeServiceImpl implements RoadTypeService {
    @Autowired
    private RoadTypeDao roadTypeDao;

    @Override
    public Optional<List<RoadTypeBean>> findAll() {
        return Optional.of(roadTypeDao.findAll());
    }
}
