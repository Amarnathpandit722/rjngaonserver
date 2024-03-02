package gov.municipal.suda.modules.watermgmt.controller;

import gov.municipal.suda.modules.property.model.master.SAFAllDropDownBean;
import gov.municipal.suda.modules.watermgmt.dto.transaction.WaterAllDropDownDto;
import gov.municipal.suda.modules.watermgmt.service.master.DropDownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
@RestController
@CrossOrigin
public class WaterDropDownController {
    @Autowired
    DropDownService dropDownService;

    @GetMapping({"/WaterAllDropDown"})
    public ResponseEntity<Optional<WaterAllDropDownDto>> getAllDropDownList() {
        return ResponseEntity.ok(dropDownService.findAllDropDown());
    }
}
