package com.drowsiness.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.drowsiness.service.AmazonS3ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class AmazonS3ClientServiceImpl implements AmazonS3ClientService
{
    private String awsS3AudioBucket;
    private AmazonS3 amazonS3;
    private static final Logger logger = LoggerFactory.getLogger(AmazonS3ClientServiceImpl.class);

    @Autowired
    public AmazonS3ClientServiceImpl(Region awsRegion, AWSCredentialsProvider awsCredentialsProvider, String awsS3AudioBucket)
    {
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                .withRegion(awsRegion.getName()).build();
        this.awsS3AudioBucket = awsS3AudioBucket;
    }

    @Async
    public void uploadFileToS3Bucket(MultipartFile multipartFile, boolean enablePublicReadAccess)
    {
        String fileName = multipartFile.getOriginalFilename();
        System.out.println(fileName);
        try {
            //creating the file in the server (temporarily)
            File file = new File(fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();

            PutObjectRequest putObjectRequest = new PutObjectRequest(this.awsS3AudioBucket, fileName, file);

            if (enablePublicReadAccess) {
                putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
            }
            this.amazonS3.putObject(putObjectRequest);
            //removing the file created in the server
            file.delete();
        } catch (IOException | AmazonServiceException ex) {
            logger.error("error [" + ex.getMessage() + "] occurred while uploading [" + fileName + "] ");
        }
    }

    @Async
    public void deleteFileFromS3Bucket(String fileName)
    {
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(awsS3AudioBucket, fileName));
        } catch (AmazonServiceException ex) {
            logger.error("error [" + ex.getMessage() + "] occurred while removing [" + fileName + "] ");
        }
    }

	public ArrayList<String> getFilesNameFromS3Bucket() {
		ArrayList<String> result = new ArrayList<>();;
		try {
			ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(awsS3AudioBucket);

			ObjectListing objects = amazonS3.listObjects(listObjectsRequest);

			while (true) {
				List<S3ObjectSummary> summaries = objects.getObjectSummaries();
				if (summaries.size() < 1) {
					break;
				}

				for (S3ObjectSummary item : summaries) {
					if (!item.getKey().endsWith("/"))
						result.add(item.getKey());
				}

				objects = amazonS3.listNextBatchOfObjects(objects);
			}
        } catch (Exception ex) {
        	ex.printStackTrace();
            logger.error("error [" + ex.getMessage() + "] occurred while read files from awsS3 ");
        }
		return result;
	}
}