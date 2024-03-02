package gov.municipal.suda.usermanagement.service;

import gov.municipal.suda.usermanagement.dao.ModuleMasterDao;
import gov.municipal.suda.usermanagement.model.ModuleMaster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ModuleMasterServiceImpl implements ModuleMasterService {

    @Autowired
    private ModuleMasterDao moduleMasterDao;

    @Override
    public List<ModuleMaster> getAllModule() {
       return moduleMasterDao.findAll();
    }
}
