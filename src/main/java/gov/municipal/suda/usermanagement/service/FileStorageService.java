package gov.municipal.suda.usermanagement.service;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

import gov.municipal.suda.usermanagement.model.FileDB;




public interface FileStorageService {

  public FileDB store(MultipartFile file) throws IOException ;

  public FileDB getFile(String id);
  
  public Stream<FileDB> getAllFiles();
  
}		
