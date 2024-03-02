package gov.municipal.suda.modules.property.controller.master;
import gov.municipal.suda.modules.property.model.master.RoadTypeBean;
import gov.municipal.suda.modules.property.service.master.RoadTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class RoadTypeController {

    @Autowired
    private RoadTypeService roadTypeService;

    @GetMapping({"/user/getAllRoadType"})
    public ResponseEntity<Optional<List<RoadTypeBean>>> fetchAllWard(){
        return ResponseEntity.ok().body(roadTypeService.findAll());
    }
}
