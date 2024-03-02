package gov.municipal.suda.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.*;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.retry.RetryPolicy;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.*;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;



import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class UploadFile {
   // private static final ExecutorService executor = Executors.newFixedThreadPool(10);

    public static AmazonS3 AWSS3ClientConnection(String accessKey, String secretKey, String region) {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSCredentialsProviderChain(new EnvironmentVariableCredentialsProvider(),
                        new SystemPropertiesCredentialsProvider(),
                        new ProfileCredentialsProvider(),new AWSStaticCredentialsProvider(awsCredentials)))
                .withRegion(region) // replace with your preferred region

                .build();

        return s3Client;
    }

    public static String AWS_Upload(String accessKey, String secretKey, String region,String bucketName, String fileSuffix, MultipartFile file) throws IOException {
        String response=null;
        if(!file.isEmpty()) {
           // byte[] bytes=file.getBytes();
            Path path;


            File convertedFile = new File(file.getOriginalFilename());
            FileUtils.writeByteArrayToFile(convertedFile, file.getBytes());

            //BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3Client = AWSS3ClientConnection(accessKey,secretKey,region);


                   /* AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSCredentialsProviderChain(new EnvironmentVariableCredentialsProvider(),
                            new SystemPropertiesCredentialsProvider(),
                            new ProfileCredentialsProvider(),new AWSStaticCredentialsProvider(awsCredentials)))
                    .withRegion(region) // replace with your preferred region
                    .build();*/


            if(fileSuffix !=null) {
//                if(fileSuffix.contains("/")) {
//                    PutObjectRequest putRequest = new PutObjectRequest(bucketName, fileSuffix, convertedFile);
//                    s3Client.putObject(putRequest);
//                }
//                else {
                    String fileName = file.getOriginalFilename();
                    String concatString = fileName.split("\\.")[1];
                    path = Paths.get(fileSuffix + "." + concatString);
                    response = fileSuffix + "." + concatString;

                    PutObjectRequest putRequest = new PutObjectRequest(bucketName, path.toString(), convertedFile);
                    s3Client.putObject(putRequest);
                //}
            }
            else {
                path= Paths.get(file.getOriginalFilename());
                response=file.getOriginalFilename();

                PutObjectRequest putRequest = new PutObjectRequest(bucketName, path.toString(), convertedFile);
                s3Client.putObject(putRequest);
            }

        }

        return response;
    }

    public static boolean AWS_Multiple_File_Upload(String accessKey, String secretKey, String region, String bucketName, String fileSuffix, List<MultipartFile> files) throws IOException {
        boolean isComplete = false;
       // BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3Client = AWSS3ClientConnection(accessKey,secretKey,region);


//                AmazonS3ClientBuilder.standard()
//                .withCredentials(new AWSCredentialsProviderChain(new EnvironmentVariableCredentialsProvider(),
//                        new SystemPropertiesCredentialsProvider(),
//                        new ProfileCredentialsProvider(),new AWSStaticCredentialsProvider(awsCredentials)))
//               // .withClientConfiguration(getClientConfiguration())
//                .withRegion(region) // replace with your preferred region
//                .build();

        TransferManager transferManager=null;

        try {
            for(MultipartFile multifile : files) {
                try {
                     transferManager = TransferManagerBuilder.standard()
                            .withS3Client(s3Client)
                            .build();
                    //Path path = null;
                    String fileName = multifile.getOriginalFilename();
                    Path path = Paths.get(fileSuffix + fileName);
                    Upload upload = transferManager.upload(bucketName, path.toString(), convertMultiPartFileToFile(multifile));
                    upload.waitForCompletion();
                    if (upload.isDone()) {
                        isComplete = true;

                    } else if (!upload.isDone()) {
                        isComplete = false;
                        break;

                    }
                }
                finally {
                    transferManager.shutdownNow(false);
                }

            }

        } catch (AmazonServiceException e) {
        System.err.println(e.getErrorMessage());
        System.exit(1);
   }
    catch (InterruptedException e) {
            throw new RuntimeException(e);
       }


        return isComplete;
    }

        public static String AWS_Upload_With_Converted_File(String accessKey, String secretKey, String region,String bucketName, String fileSuffix, MultipartFile file) throws IOException {
        String response=null;
        if(!file.isEmpty()) {
            // byte[] bytes=file.getBytes();
            Path path;
            if(fileSuffix !=null) {
                String fileName=file.getOriginalFilename();
                String concatString=fileName.split("\\.")[1];
                path= Paths.get(fileSuffix+"."+concatString);
                response=fileSuffix+"."+concatString;
            }
            else {
                path= Paths.get(file.getOriginalFilename());
                response=file.getOriginalFilename();
            }

            File convertedFile = convertMultiPartFileToFile(file);
            //FileUtils.writeByteArrayToFile(convertedFile, file.getBytes());

           // BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3Client = AWSS3ClientConnection(accessKey,secretKey,region);

                   /* AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSCredentialsProviderChain(new EnvironmentVariableCredentialsProvider(),
                            new SystemPropertiesCredentialsProvider(),
                            new ProfileCredentialsProvider(),new AWSStaticCredentialsProvider(awsCredentials)))
                    .withRegion(region) // replace with your preferred region

                    .build();*/



            PutObjectRequest putRequest = new PutObjectRequest(bucketName, path.toString(), convertedFile);
            s3Client.putObject(putRequest);
        }

        return response;
    }



    public static boolean deleteMultipleObjectFromAWS(String accessKey, String secretKey, String region,String bucketName, String locationPrefix) {
        boolean isFinished=false;
       // BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3Client = AWSS3ClientConnection(accessKey,secretKey,region);
               /* AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSCredentialsProviderChain(new EnvironmentVariableCredentialsProvider(),
                        new SystemPropertiesCredentialsProvider(),
                        new ProfileCredentialsProvider(),new AWSStaticCredentialsProvider(awsCredentials)))
                .withRegion(region) // replace with your preferred region
                .build();*/

        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(bucketName)
                .withPrefix(locationPrefix);
        ObjectListing objectListing = s3Client.listObjects(listObjectsRequest);
        //List<String> objectKeys = new ArrayList<>();
        for(S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            //objectKeys.add(objectSummary.getKey());
            s3Client.deleteObject(bucketName, objectSummary.getKey());
            isFinished=true;
        }

    /*    DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(bucketName)
                .withKeys("102/")
                .withQuiet(false);
        DeleteObjectsResult res= s3Client.deleteObjects(multiObjectDeleteRequest);
        int successfulDeletes =res.getDeletedObjects().size();
        if(successfulDeletes > 0) {
            isFinished=true;
        }*/

        //isFinished=true;

        // Delete the sample objects.


        return isFinished;
    }
    public static List<String> getKeys(String bucketName,String locationPrefix, AmazonS3 s3Client) {
        List<String> objectKeys = new ArrayList<>();
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(bucketName)
                .withPrefix(locationPrefix);
        ObjectListing objectListing = s3Client.listObjects(listObjectsRequest);
        for(S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            objectKeys.add(objectSummary.getKey());
        }
        return objectKeys;
    }
    public static List<String> generatedPreSignedUrls(String accessKey, String secretKey, String region,String bucketName, String locationPrefix) {
        AmazonS3 s3Client = AWSS3ClientConnection(accessKey,secretKey,region);
        List<String> keys =getKeys(bucketName,locationPrefix, s3Client);

        return keys.stream()
                .map(key -> {
                    GeneratePresignedUrlRequest generatePresignedUrlRequest =
                            new GeneratePresignedUrlRequest(bucketName, key)
                                    .withMethod(HttpMethod.GET)
                                    .withExpiration(new Date(System.currentTimeMillis() + 3600 * 1000)); // 1 hour expiration
                    return s3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
                })
                .collect(Collectors.toList());
    }
    private static File convertMultiPartFileToFile(MultipartFile approval_letter) throws IOException {
        File convertedFile = new File(approval_letter.getOriginalFilename());
        FileUtils.writeByteArrayToFile(convertedFile, approval_letter.getBytes());
        return convertedFile;
    }

     private static ClientConfiguration getClientConfiguration() {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setRetryPolicy(new RetryPolicy(null, null, 3, false));
        return clientConfiguration;
    }



    public static void upload(final String bucketName, final File filePath,final AmazonS3 amazonS3) {
        //Future<Boolean> isComplete = null;
        TransferManager tm = TransferManagerBuilder.standard()
                .withS3Client(amazonS3)
                //.withMultipartUploadThreshold((long) (50 * 1024 * 1025))
                .build();

        final String fileName = filePath.getName();
        try {
            Upload upload = tm.upload(bucketName, fileName, filePath);
            upload.waitForCompletion();
//            if(upload.isDone()) {
//                isComplete.isDone();
//            }
            //log.info("Successfully uploaded file = " + fileName);

        } catch (Exception e) {
            e.printStackTrace();
            //log.info("Upload failed for file = " + fileName);
            //log.error(e);
        }
        //return isComplete;
    }



}
