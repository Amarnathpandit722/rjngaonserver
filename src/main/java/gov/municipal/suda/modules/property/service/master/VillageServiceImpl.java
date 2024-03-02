package gov.municipal.suda.modules.property.service.master;


import gov.municipal.suda.modules.property.dao.master.VillageDao;

import gov.municipal.suda.modules.property.dao.master.*;
import gov.municipal.suda.modules.property.model.master.*;
import gov.municipal.suda.modules.property.model.master.*;
import org.json.simple.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VillageServiceImpl implements VillageService{

    @Autowired
    VillageDao villageDao;

        @Autowired
        WardDao wardDao;
        @Autowired
        ZoneDao zoneDao;
        @Autowired
        FloorDao floorDao;
        @Autowired
        ArvRangeDao arvRangeDao;
        @Autowired
        ArvRateDao arvRateDao;
        @Override
        public List<VillageBean> fetchAllVillage() {
            return villageDao.findAll();
        }
        @Override
        public Object getAllVWZ() {
            List<VillageBean> village= villageDao.findAll();
            List<WardBean> ward= wardDao.findAll();
            List<ZoneBean> zone= zoneDao.findAll();
            List<FloorBean> floor= floorDao.findAll();
            List<ArvRangeBean> arvRange= arvRangeDao.findAll();
            List<ArvRateBean> arvRate= arvRateDao.findAll();
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("Village",village);
            jsonObject.put("Ward",ward);
            jsonObject.put("Zone",zone);
            jsonObject.put("Floor",floor);
            jsonObject.put("ArvRate",arvRate);
            jsonObject.put("ArvRange",arvRange);
            return jsonObject;

        }
    }