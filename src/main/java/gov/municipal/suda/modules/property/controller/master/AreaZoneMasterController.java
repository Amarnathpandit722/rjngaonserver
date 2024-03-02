package gov.municipal.suda.modules.property.controller.master;

import gov.municipal.suda.modules.property.model.master.AreaZoneMasterBean;
import gov.municipal.suda.modules.property.service.master.AreaZoneMasterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class AreaZoneMasterController {
    @Autowired
    private AreaZoneMasterServiceImpl areaZoneMasterService;

    @GetMapping({"/FindAllAreaZone"})
    public ResponseEntity<Optional<List<AreaZoneMasterBean>>> findAllEffectYearRateCharge() {
        return ResponseEntity.ok().body(areaZoneMasterService.findAllAreaZone());
    }
}
