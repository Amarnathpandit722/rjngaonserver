package gov.municipal.suda.modules.property.controller.master;

import gov.municipal.suda.modules.property.dto.PropertyGeoLocationRespnseDto;
import gov.municipal.suda.modules.property.service.master.PropertyGeoLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@CrossOrigin
public class GeoTaggingController {

    @Autowired
    PropertyGeoLocationService propertyGeoLocationService;

    @PostMapping({"/user/property/geo-tag-entry"})
    public ResponseEntity<String> PropertyGeoEntry(@RequestParam("property_no") String propertyNo,
                                                            @RequestParam("long") String longitude,
                                                            @RequestParam("lat") String latitude,
                                                            @RequestParam("files") List<MultipartFile> files
                                                            ) throws IOException, ParseException {
        propertyGeoLocationService.propertyGeoEntry(propertyNo,longitude,latitude,files);

        return ResponseEntity.ok("Success");


    }
    @PutMapping({"/user/property/geo-tag-update"})
    public ResponseEntity<String> PropertyGeoUpdate(@RequestParam("property_no") String propertyNo,
                                                            @RequestParam("long") String longitude,
                                                            @RequestParam("lat") String latitude,
                                                            @RequestParam("files") List<MultipartFile> files
    ) throws IOException, ParseException {
        propertyGeoLocationService.propertyGeoLocationUpdate(propertyNo,longitude,latitude,files);
        return ResponseEntity.ok("Success");
    }

    @GetMapping({"/user/property/geo-tag-url"})
    public ResponseEntity<PropertyGeoLocationRespnseDto> getPropertyGeoUrls(@RequestParam("prop_id") Long prop_id) {
        return ResponseEntity.ok(propertyGeoLocationService.generatePreSignedURLForPropertyGeoLocation(prop_id));
    }

}
