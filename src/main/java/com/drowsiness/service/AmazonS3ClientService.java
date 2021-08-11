package com.drowsiness.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

public interface AmazonS3ClientService
{
    void uploadFileToS3Bucket(MultipartFile multipartFile, boolean enablePublicReadAccess);

    void deleteFileFromS3Bucket(String fileName);
    
    ArrayList<String> getFilesNameFromS3Bucket();
}
