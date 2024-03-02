package gov.municipal.suda.modules.wastemgmt.controller;

import gov.municipal.suda.modules.wastemgmt.dto.ConsumerEntryDTO;
import gov.municipal.suda.modules.wastemgmt.dto.RenterEntryDTO;
import gov.municipal.suda.modules.wastemgmt.service.master.RenterMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class RenterMasterController {
    @Autowired
    RenterMasterService renterMasterService;
    @PostMapping({"/addRenter"})
    public ResponseEntity<String> addRenter(@Valid @RequestBody RenterEntryDTO renterEntryDTO) throws SQLException {
        String results= String.valueOf(renterMasterService.addRenter(renterEntryDTO));
        if (null==results) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error in Renter Controller");
        }
        return ResponseEntity.ok(results);
    }
}
