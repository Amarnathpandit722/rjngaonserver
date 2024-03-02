package gov.municipal.suda.modules.property.controller.master;

import gov.municipal.suda.modules.property.model.master.VillageBean;
import gov.municipal.suda.modules.property.service.master.VillageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class VillageController {

    @Autowired
    private VillageService villageService;
    @GetMapping({"/getAllVillage"})
    public ResponseEntity<List<VillageBean>> fetchAllVillage(){
        return ResponseEntity.ok().body(villageService.fetchAllVillage());
    }

    @GetMapping({"/getVill_Ward_Zone_Floor_Rate_Range"})
    public ResponseEntity<Object> getAllVWZ(){
        return ResponseEntity.ok().body(villageService.getAllVWZ());
    }

}
