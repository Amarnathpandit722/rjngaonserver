package gov.municipal.suda.modules.property.controller.master;


import gov.municipal.suda.modules.property.service.master.ArvRangeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@CrossOrigin
public class ArvRangeController {

    @Autowired
    private ArvRangeServiceImpl arvRangeService;
    @GetMapping({"/FindAllArvRange"})
    public ResponseEntity<List<Object>> findAllArvRange() {

        return ResponseEntity.ok().body(arvRangeService.getAllArvRange());
    }
}
