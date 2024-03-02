package gov.municipal.suda.modules.property.service.master;

import gov.municipal.suda.modules.property.dao.master.ArvRateDao;
import gov.municipal.suda.modules.property.dao.master.EduAndCompositRateDao;
import gov.municipal.suda.modules.property.dao.master.EffectYearRateChangeDao;
import gov.municipal.suda.modules.property.dao.master.VacantLandRateDao;
import gov.municipal.suda.modules.property.model.master.ArvRateBean;
import gov.municipal.suda.modules.property.model.master.EduAndCompositeRateBean;
import gov.municipal.suda.modules.property.model.master.EffectYearOfRateChangeBean;
import gov.municipal.suda.modules.property.model.master.VacantLandRateBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
public class AllTypeOfRateServiceImpl implements AllTypesOfRateService{
    @Autowired
    private VacantLandRateDao vacantLandRateDao;
   /* @Autowired
    ArvRateDao arvRateDao;*/
    @Autowired
    EduAndCompositRateDao eduAndCompositRateDao;

    @Autowired
    EffectYearRateChangeDao effectYearRateChargeDao;

    @Override
    public Optional<List<VacantLandRateBean>> findVacantLandRate() {
        return Optional.of(vacantLandRateDao.findAll().stream().filter(v-> v.getStatus().equals("1")).collect(Collectors.toList()));
    }

    public Optional<ArvRateBean> findArvRateByUsesTypeAndFYYear(Long zone_id, Long road_id, String const_type, Long uses_type_id, String effect_year) {

        //        Optional<ArvRateBean> arvRateBean= arvRateDao.findArvRateByUsesTypeAndFYear(zone_id,road_id,const_type,uses_type_id,fy_year);
//        ArvRateBean result=arvRateBean.get();
//        log.info("Result {} ",result);
//        ArvRateResponseDTO arvRateResponseDTO=new ArvRateResponseDTO();
//        arvRateResponseDTO.setBuilding_rate(result.getBuilding_rate());
//        arvRateResponseDTO.setLand_rate(result.getLand_rate());
//        arvRateResponseDTO.setId(result.getId());

       // SimpleDateFormat format = new SimpleDateFormat("yyyy");
        //Date date = format.parse(String.valueOf(fy_year));
       // log.info("Current Year is {} " , fy_year);
        //currentYear = format.format(now);

        //DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY")
                //.withLocale(Locale.ENGLISH);

        //LocalDate date = formatter.parseLocalDate(fy_year);

       // log.info("Year Conversion {}", date.year());

       // return arvRateDao.findArvRateByUsesTypeAndFYear(zone_id,road_id,const_type,uses_type_id,effect_year,"eee");
    return null;
    }

    @Override
    public Optional<List<EduAndCompositeRateBean>> getAllEduAndCompositeRate() {
        return Optional.of(eduAndCompositRateDao.findAll());
    }


    @Override
    public Optional<List<EffectYearOfRateChangeBean>> findAllEffectYearCharge() {
        return Optional.of(effectYearRateChargeDao.findAll());
    }

//    @Override
//    public List<ArvRateEffectiveDateDTO> findEffectiveDate() {
//        return arvRateDao.findEffectiveDate();
//    }
}
