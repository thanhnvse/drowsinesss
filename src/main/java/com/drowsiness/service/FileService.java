package com.drowsiness.service;

import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface FileService {
    String uploadFile(File file, String fileName) throws IOException, FirebaseAuthException, InterruptedException, ExecutionException; // used to upload a file
    File convertToFile(MultipartFile multipartFile, String fileName) throws IOException; // used to convert MultipartFile to File
    String getExtension(String fileName);  // used to get extension of a uploaded file
    Object upload(MultipartFile multipartFile);
    Object download(String fileName) throws IOException;
    String getImgUrl(MultipartFile multipartFile) throws IOException, ExecutionException, InterruptedException;
}
