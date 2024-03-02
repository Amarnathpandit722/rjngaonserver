package gov.municipal.suda.usermanagement.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import gov.municipal.suda.usermanagement.model.ModuleMaster;
import gov.municipal.suda.usermanagement.service.ModuleMasterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class ModuleMasterController {

    @Autowired
    private ModuleMasterServiceImpl moduleMasterService;

    @Autowired
   private ObjectMapper objectMapper;

    @GetMapping("/getAllModule")
    public ResponseEntity<List<ModuleMaster>> getAllModule() {
       List<ModuleMaster> results=moduleMasterService.getAllModule();
           return  ResponseEntity.accepted().body(results);
    }
}
