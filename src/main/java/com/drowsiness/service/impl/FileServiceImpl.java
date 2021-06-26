package com.drowsiness.service.impl;

import com.drowsiness.dto.response.ApiResult;
import com.drowsiness.service.FileService;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class FileServiceImpl implements FileService {
//    private static final String UID = "some-uid";

    @Override
    public String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of("drowsiness-316609.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();
        Credentials credentials = GoogleCredentials.fromStream(
                new FileInputStream("src/main/resources/serviceAccountKey.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        //create custom tokens
//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setCredentials((GoogleCredentials) credentials)
//                .build();
//
//        FirebaseApp.initializeApp(options);
//        String customToken = FirebaseAuth.getInstance().createCustomTokenAsync(UID).get();
//        System.out.println(customToken);

        return String.format("https://firebasestorage.googleapis.com/v0/b/drowsiness-316609.appspot.com/o/%s?alt=media", URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    @Override
    public File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }

    @Override
    public String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    @Override
    public Object upload(MultipartFile multipartFile) {
        try {
            String fileName = multipartFile.getOriginalFilename();                        // to get original file name
            fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));  // to generated random string values for file name.

            File file = this.convertToFile(multipartFile, fileName);                      // to convert multipartFile to File
            String TEMP_URL = this.uploadFile(file, fileName);                                   // to get uploaded file link
            file.delete();                                                                // to delete the copy of uploaded file stored in the project folder
            return new ApiResult<>(TEMP_URL,"Successfully Uploaded !");                     // Your customized response
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResult<>(e, "Unsuccessfully Uploaded! . Status 500");
        }

    }

    @Override
    public Object download(String fileName) throws IOException {
        String destFileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));     // to set random string for destination file name
        String destFilePath = "F:\\New folder\\" + destFileName;                                    // to set destination file path

//        String destFilePath = "F:\\New folder\\"+fileName+".jpg";                                    // to set destination file path
        ////////////////////////////////   Download  ////////////////////////////////////////////////////////////////////////
        Credentials credentials = GoogleCredentials.fromStream(
                new FileInputStream("src/main/resources/serviceAccountKey.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        Blob blob = storage.get(BlobId.of("drowsiness-316609.appspot.com", fileName));
        blob.downloadTo(Paths.get(destFilePath));
        return new ApiResult<>("200", "Successfully Downloaded!");
    }

    @Override
    public String getImgUrl(MultipartFile multipartFile) throws IOException, ExecutionException, InterruptedException {
        String fileName = multipartFile.getOriginalFilename();                        // to get original file name
        fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));  // to generated random string values for file name.

        File file = this.convertToFile(multipartFile, fileName);                      // to convert multipartFile to File
        String TEMP_URL = this.uploadFile(file, fileName);                                   // to get uploaded file link
        file.delete();
        return TEMP_URL;
    }
}
