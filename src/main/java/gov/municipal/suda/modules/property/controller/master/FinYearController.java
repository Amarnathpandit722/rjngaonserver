package gov.municipal.suda.modules.property.controller.master;

import gov.municipal.suda.modules.property.model.master.FinYearBean;
import gov.municipal.suda.modules.property.service.master.FinYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class FinYearController {

    @Autowired
    private FinYearService finYearService;

    @GetMapping({"/user/FindAllFinYear"})
    public ResponseEntity<Optional<List<FinYearBean>>> findAllFinYear() {
        return ResponseEntity.ok().body(finYearService.findAllFinYear());
    }

}
