package gov.municipal.suda.util;

import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
@Slf4j
public class DeleteFilesAndFolder {

    public static void deleteFileFromCurrentDirectory(String fileName) throws IOException {
        String absolutePath=System.getProperty("user.dir");
        Path path = FileSystems.getDefault().getPath(absolutePath+"/"+fileName);
       // log.info("Current Path {}",path);
        boolean isTrue=Files.deleteIfExists(path);
        if(isTrue) {
          //  log.info("File deleted......");
        }
    }
}
