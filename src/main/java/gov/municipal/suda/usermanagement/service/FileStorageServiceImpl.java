package gov.municipal.suda.usermanagement.service;

import gov.municipal.suda.modules.property.model.master.PropertyDocDtlBean;
import gov.municipal.suda.usermanagement.dao.FileDBRepository;
import gov.municipal.suda.usermanagement.model.FileDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.stream.Stream;

@Service
public class FileStorageServiceImpl implements FileStorageService{

    @Autowired
    private FileDBRepository fileDBRepository;

    public FileDB store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileDB FileDB = new FileDB(fileName, file.getContentType(), file.getBytes());
        return fileDBRepository.save(FileDB);
    }

    public FileDB getFile(String id) {
        return fileDBRepository.findById(id).get();
    }

    public Stream<FileDB> getAllFiles() {
        return fileDBRepository.findAll().stream();
    }

}