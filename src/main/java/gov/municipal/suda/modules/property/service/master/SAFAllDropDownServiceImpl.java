package gov.municipal.suda.modules.property.service.master;

import gov.municipal.suda.modules.property.dao.master.*;
import gov.municipal.suda.modules.property.model.master.FloorBean;
import gov.municipal.suda.modules.property.model.master.SAFAllDropDownBean;
import gov.municipal.suda.modules.property.model.master.WardBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@Slf4j
public class SAFAllDropDownServiceImpl implements SAFAllDropDownService {

    @Autowired
    ZoneDao zoneDao;
    @Autowired
    WardDao wardDao;
    @Autowired
    FloorDao floorDao;
    @Autowired
    BuildingTypeDao buildingTypeDao;
    @Autowired
    PropertyTypeDao propertyTypeDao;
    @Autowired
    OccupationTypeDao occupationTypeDao;
    @Autowired
    UsesTypeDao usesTypeDao;

    @Autowired
    RoadTypeDao roadTypeDao;
    @Autowired
    EffectYearRateChangeDao effectYearRateChargeDao;
    @Autowired
    EntryTypeDao entryTypeDao;

    @Autowired
    FinYearDao finYearDao;
    @Autowired
    FinYearService  finYearService;


    @Override
    public Optional<SAFAllDropDownBean> findAllDropDown() {
        SAFAllDropDownBean safAllDropDownBean=new SAFAllDropDownBean();
        safAllDropDownBean.setZone(zoneDao.findAll().stream().filter(z ->z.getStatus().equals("1")).collect(Collectors.toList()));
//        safAllDropDownBean.setWard(wardDao.findAll().stream().filter(z ->z.getStatus().equals("1")).collect(Collectors.toList()));
        
        safAllDropDownBean.setWard(wardDao.findAll().stream().filter(z -> z.getStatus().equals("1")).sorted(Comparator.comparingLong(WardBean::getId)).collect(Collectors.toList()));

        safAllDropDownBean.setFloor(floorDao.findAll().stream().filter(z ->z.getStatus().equals("1")).sorted(Comparator.comparingLong(FloorBean::getId)).collect(Collectors.toList()));
        
        safAllDropDownBean.setBuilding_type(buildingTypeDao.findAll().stream().filter(z ->z.getStatus().equals("1")).collect(Collectors.toList()));
        
        safAllDropDownBean.setProperty_type(propertyTypeDao.findAll().stream().filter(z ->z.getStatus().equals("1")).collect(Collectors.toList()));
        
        safAllDropDownBean.setOccupation_type(occupationTypeDao.findAll().stream().filter(z ->
                z.getStatus().equals("1")).collect(Collectors.toList()));
        safAllDropDownBean.setUses_type(usesTypeDao.findAll().stream().filter(z ->
                z.getStatus().equals("1")).collect(Collectors.toList()));
        
        safAllDropDownBean.setRoadType(roadTypeDao.findAll());
        
        
        safAllDropDownBean.setArvRateEffectiveYear(effectYearRateChargeDao.findAll().stream().
                filter(e-> e.getStatus().equals("1")).collect(Collectors.toList()));
        safAllDropDownBean.setEntry_type(entryTypeDao.findAll().stream().
                filter(e-> e.getStatus().equals("1")).collect(Collectors.toList()));
        safAllDropDownBean.setFinancial_year(finYearDao.findAll().stream().
                filter(e -> e.getStatus().equals("1")).collect(Collectors.toList()));

       // safAllDropDownBean.setFinancial_year(finYearService.getFinancialYear());


        return Optional.of(safAllDropDownBean);
    }
}
