package gov.municipal.suda.modules.property.service.master;

import gov.municipal.suda.modules.property.dao.master.WardDao;
import gov.municipal.suda.modules.property.model.master.WardBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WardServiceImpl implements WardService {
    @Autowired
    private WardDao wardDao;
    public List<WardBean> fetchAllWard(){

           return wardDao.findAll();

    }
}
