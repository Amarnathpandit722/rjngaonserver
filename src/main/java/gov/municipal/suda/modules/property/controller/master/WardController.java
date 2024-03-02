package gov.municipal.suda.modules.property.controller.master;

import gov.municipal.suda.modules.property.model.master.WardBean;
import gov.municipal.suda.modules.property.service.master.WardService;
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
public class WardController {
    @Autowired
    private WardService wardService;
    @GetMapping({"/getAllWard"})
    public ResponseEntity<List<WardBean>> fetchAllWard(){
        return ResponseEntity.ok().body(wardService.fetchAllWard());
    }
}
