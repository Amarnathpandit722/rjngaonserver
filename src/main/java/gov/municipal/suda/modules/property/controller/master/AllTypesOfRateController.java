package gov.municipal.suda.modules.property.controller.master;

import gov.municipal.suda.modules.property.model.master.ArvRateBean;
import gov.municipal.suda.modules.property.model.master.EduAndCompositeRateBean;
import gov.municipal.suda.modules.property.model.master.EffectYearOfRateChangeBean;
import gov.municipal.suda.modules.property.model.master.VacantLandRateBean;
import gov.municipal.suda.modules.property.service.master.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class AllTypesOfRateController {

    @Autowired
    AllTypeOfRateServiceImpl allTypeOfRateService;

    @GetMapping({"/getAllVacantLandRate"})
    public ResponseEntity<Optional<List<VacantLandRateBean>>> fetchAllWard(){
        return ResponseEntity.ok().body(allTypeOfRateService.findVacantLandRate());
    }

    @GetMapping({"/FindAllEffectYearRateCharge"})
    public ResponseEntity<Optional<List<EffectYearOfRateChangeBean>>> findAllEffectYearRateCharge() {
        return ResponseEntity.ok().body(allTypeOfRateService.findAllEffectYearCharge());
    }

    @GetMapping({"/FindArvRateByUsesTypeAndFYear/{zone_id}/{road_id}/{const_type}/{uses_type_id}/{F_Year}"})
    public ResponseEntity<Optional<ArvRateBean>> findArvRateByUsesTypeAndFYear(
            @PathVariable("zone_id") Long zone_id, @PathVariable("road_id") Long road_id,
            @PathVariable("const_type") String const_type,
            @PathVariable("uses_type_id") Long uses_type_id, @PathVariable("F_Year") String F_Year
    ) throws ParseException {

        return ResponseEntity.ok().body(allTypeOfRateService.findArvRateByUsesTypeAndFYYear(zone_id,road_id,
                const_type,uses_type_id,F_Year));
    }

    @GetMapping({"/FindAllEduAndCompositeRate"})
    public ResponseEntity<Optional<List<EduAndCompositeRateBean>>> getAllEduAndCompositeRate() {
        return ResponseEntity.ok().body(allTypeOfRateService.getAllEduAndCompositeRate());
    }



//    @GetMapping({"/FindAllArvRateEffectiveDate"})
//    public ResponseEntity<List<ArvRateEffectiveDateDTO>> getAllArvRateEffectiveYear() {
//        return ResponseEntity.ok().body(allTypeOfRateService.findEffectiveDate());
//    }

}
