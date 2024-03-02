package gov.municipal.suda.modules.property.service.master;

import gov.municipal.suda.modules.property.dao.master.ZoneDao;
import gov.municipal.suda.modules.property.model.master.ZoneBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ZoneServiceImpl implements ZoneService {

    @Autowired
    private ZoneDao zoneDao;
    @Override
    public List<ZoneBean> fetchAllZone() {

        return zoneDao.findAll();

    }
}
