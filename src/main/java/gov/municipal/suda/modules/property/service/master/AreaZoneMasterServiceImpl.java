package gov.municipal.suda.modules.property.service.master;

import gov.municipal.suda.modules.property.dao.master.AreaZoneMasterDao;
import gov.municipal.suda.modules.property.model.master.AreaZoneMasterBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AreaZoneMasterServiceImpl implements AreaZoneMasterService{
    @Autowired
    private AreaZoneMasterDao areaZoneMasterDao;

    @Override
    public Optional<List<AreaZoneMasterBean>> findAllAreaZone() {
        return Optional.of(areaZoneMasterDao.findAll().stream().filter(a -> a.getStatus().
                        equals("1")).collect(Collectors.toList()));
    }
}
