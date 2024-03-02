package gov.municipal.suda.modules.property.controller.master;

import gov.municipal.suda.modules.property.model.master.PropertyTypeBean;
import gov.municipal.suda.modules.property.service.master.PropertyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class PropertyTypeController {
    @Autowired
    private PropertyTypeService propertyTypeService;

    @GetMapping({"/user/PropertyType"})
    public ResponseEntity<Optional<List<PropertyTypeBean>>> getAllPropertyType() {
        return ResponseEntity.ok().body(propertyTypeService.findAll());

    }
}
