package gov.municipal.suda.modules.property.service.master;

import gov.municipal.suda.modules.property.dto.PropertyGeoLocationDTO;
import gov.municipal.suda.modules.property.dto.PropertyGeoLocationRespnseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PropertyGeoLocationService {
    void propertyGeoEntry(String propertyNo,String longitude,String latitude,List<MultipartFile> files) throws IOException;
    void propertyGeoLocationUpdate(String propertyNo, String longitude,String latitude,List<MultipartFile> files) throws IOException;
    PropertyGeoLocationRespnseDto generatePreSignedURLForPropertyGeoLocation(Long prop_id);
}
