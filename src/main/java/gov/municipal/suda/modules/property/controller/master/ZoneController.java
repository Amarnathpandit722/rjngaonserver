package gov.municipal.suda.modules.property.controller.master;

import gov.municipal.suda.modules.property.model.master.ZoneBean;
import gov.municipal.suda.modules.property.service.master.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class ZoneController {

    @Autowired
    private ZoneService zoneService;
    @GetMapping({"/getAllZone"})
    public ResponseEntity<List<ZoneBean>> fetchAllZone(){
       return ResponseEntity.ok().body(zoneService.fetchAllZone());
    }
}
