package gov.municipal.suda.modules.property.service.master;

import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.modules.property.dao.master.PropertyGeoLocationDao;
import gov.municipal.suda.modules.property.dao.master.PropertyMasterDao;
import gov.municipal.suda.modules.property.dto.PropertyGeoLocationRespnseDto;
import gov.municipal.suda.modules.property.model.master.PropertyGeoLocationBean;
import gov.municipal.suda.modules.property.model.master.PropertyMasterBean;
import gov.municipal.suda.util.Constants.Constants;
import gov.municipal.suda.util.DeleteFilesAndFolder;
import gov.municipal.suda.util.UploadFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PropertyGeoLocationServiceImpl implements PropertyGeoLocationService{
    @Autowired
    PropertyGeoLocationDao propertyGeoLocationDao;
    @Autowired
    PropertyMasterDao propertyMasterDao;
    @Value("${aws.accessKeyId}")
    private String accessKey;
   @Value("${aws.secretKey}")
   private String secretKey;
   @Value("${aws.region}")
   private String region;
   @Value("${aws.property.geolocation.buketName}")
   private String bucketName;

    @Override
    @Transactional(rollbackOn= BadRequestException.class)
    public void propertyGeoEntry(String propertyNo, String longitude,String latitude, List<MultipartFile> files) throws IOException {
        boolean isUploadSuccessful=false;
        try {
            Long prop_id;
            PropertyMasterBean propertyBean=propertyMasterDao.findPropertyByPropNo(propertyNo);
            prop_id=propertyBean.getId();
            if(prop_id!=null) {
               List<PropertyGeoLocationBean> propGeoBean=propertyGeoLocationDao.findPropertyGeoLocationByPropId(prop_id);
               if(propGeoBean.isEmpty()) {
                   String prefix =prop_id + Constants.AWS_FOLDER_CREATION_SUFIX;
                   PropertyGeoLocationBean bean = new PropertyGeoLocationBean();
                   bean.setProperty_pictures_location(prefix);
                   bean.setProperty_latitude(latitude);
                   bean.setProperty_id(prop_id);
                   bean.setProperty_longitude(longitude);
                   PropertyGeoLocationBean geoEntry=propertyGeoLocationDao.save(bean);

					
					  if(geoEntry.getProp_geolocation_id()>0 && !files.isEmpty()) {
					  isUploadSuccessful=UploadFile.AWS_Multiple_File_Upload(accessKey, secretKey,
					  region, bucketName, prefix, files); }
					 
                  if(isUploadSuccessful==false) {
                      throw new BadRequestException("Upload not successful");
                  }
               }
               else if (!propGeoBean.isEmpty()) {
                   throw new BadRequestException("This property already has Geo Entry");
               }
            } else if (prop_id==null) {
                throw new BadRequestException("Property id can't be blank for Property Geo Location");
            }
        }
        catch(Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        finally {
            if(!files.isEmpty()) {
                for (MultipartFile file : files) {
                    DeleteFilesAndFolder.deleteFileFromCurrentDirectory(file.getOriginalFilename());
                }
            }
        }
    }

    @Override
    @Transactional(rollbackOn= BadRequestException.class)
    public void propertyGeoLocationUpdate(String  propertyNo, String longitude,String latitude,List<MultipartFile> files) throws IOException {
        boolean isUploadSuccessful=false;
        boolean isTrue=false;
        try {
            Long prop_id;
            PropertyMasterBean propertyBean=propertyMasterDao.findPropertyByPropNo(propertyNo);
            prop_id=propertyBean.getId();
           if(prop_id !=null) {
               String prefix=null;

               PropertyGeoLocationBean itHaveNewId=null;
                   List<PropertyGeoLocationBean> propertyGeoList = propertyGeoLocationDao.findPropertyGeoLocationByPropId(prop_id);
                   if (!propertyGeoList.isEmpty()) {
                       for (PropertyGeoLocationBean propGeoBean : propertyGeoList) {
                           prefix=propGeoBean.getProperty_pictures_location();
                           Optional<PropertyGeoLocationBean> bean = propertyGeoLocationDao.findById(propGeoBean.getProp_geolocation_id());
                           if (bean.isPresent()) {
                               bean.get().setProperty_id(prop_id);
                               bean.get().setProperty_latitude(latitude);
                               bean.get().setProperty_longitude(longitude);
                               itHaveNewId=propertyGeoLocationDao.save(bean.get());
                           }
                       }
                   }
               if(itHaveNewId.getProp_geolocation_id() > 0) {
                   isTrue = UploadFile.deleteMultipleObjectFromAWS(accessKey, secretKey, region, bucketName, itHaveNewId.getProperty_pictures_location());
               if (isTrue) {
                   if (!files.isEmpty()) {
                       isUploadSuccessful=UploadFile.AWS_Multiple_File_Upload(accessKey, secretKey, region, bucketName, itHaveNewId.getProperty_pictures_location(), files);
                   }
                   if(isUploadSuccessful==false) {
                       throw new BadRequestException("Upload not successful");
                   }
               } else {
                   throw new BadRequestException("Delete process of bucket is not successful");
               }

               }
        else if(itHaveNewId.getProp_geolocation_id()==0){
                   throw new BadRequestException("updated not successful");
               }
           }
           else if (prop_id==null) {
               throw  new BadRequestException("Property Id can't be blank for property GeoLocation update");

           }
        } catch(Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        finally {
            if(!files.isEmpty()) {
                for (MultipartFile file : files) {
                    DeleteFilesAndFolder.deleteFileFromCurrentDirectory(file.getOriginalFilename());
                }
            }
        }

    }

    @Override
    public PropertyGeoLocationRespnseDto generatePreSignedURLForPropertyGeoLocation(Long prop_id) {
        PropertyGeoLocationRespnseDto results=new PropertyGeoLocationRespnseDto();
        if(prop_id==null) {
            throw new BadRequestException("Property Id or Number must not be null/empty");
        }
        List<PropertyGeoLocationBean> propertyGeoList = propertyGeoLocationDao.findPropertyGeoLocationByPropId(prop_id);
        if(propertyGeoList.isEmpty()) {
            throw new BadRequestException("In Property Geo Location record not found");
        }
        if(propertyGeoList.get(0).getProperty_pictures_location().isEmpty()) {
            throw new BadRequestException("Property Location not found in the table");
        }
		
		  List<String> keys= UploadFile.generatedPreSignedUrls(accessKey, secretKey, region, bucketName,propertyGeoList.get(0).getProperty_pictures_location());
		  if(keys.isEmpty())
		  { 
			  throw new		  BadRequestException("image urls not present or save");
			  }
		  
		  results.setProperty_latitude(propertyGeoList.get(0).getProperty_latitude());
		  results.setProperty_longitude(results.getProperty_latitude());
		  results.setUrls(keys);
		 
log.info("results {} " , results);
        return results;
    }
}
