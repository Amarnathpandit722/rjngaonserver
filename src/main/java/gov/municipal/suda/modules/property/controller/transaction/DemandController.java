package gov.municipal.suda.modules.property.controller.transaction;


import gov.municipal.suda.modules.property.dto.DemandRequestDto;
import gov.municipal.suda.modules.property.service.transaction.DemandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@Slf4j
public class DemandController {

    @Autowired
    private DemandService demandService;

    @PostMapping({"/user/createDemandEntry"})
    public ResponseEntity<String> demandEntry(@RequestBody List<DemandRequestDto> demandRequest, @RequestParam("user_id") Long user_id
                                             ) throws Exception {

        demandService.createDemand(demandRequest,user_id);

        return ResponseEntity.ok("Demand Generate Successfully");
    }
}
