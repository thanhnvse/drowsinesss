package com.drowsiness.controller;


import com.drowsiness.dto.firmware.FirmwareDTO;
import com.drowsiness.dto.response.ApiResult;
import com.drowsiness.service.AmazonS3ClientService;
import com.drowsiness.utils.GitControl;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping({"/api/v1"})
public class S3Controller {

    @Autowired
    private AmazonS3ClientService amazonS3ClientService;


    @PostMapping(value = {"/dataset"}, consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadAndTrainModel(@ModelAttribute FirmwareDTO fwDTO) throws GitAPIException, IOException {
        ApiResult<?> apiResult;
        try {
            this.amazonS3ClientService.uploadFileToS3Bucket(fwDTO.getFile(), true);
            apiResult = new ApiResult(fwDTO.getFile().getName(), "Upload dataset success, please wait for training process!");
        } catch (Exception e) {
            apiResult = new ApiResult("Error when uploading!");
        }

        GitControl gitControl = new GitControl();
        gitControl.cloneRepo();

        

        gitControl.cleanDir();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
    }

//    @DeleteMapping
//    public Map<String, String> deleteFile(@RequestParam("file_name") String fileName)
//    {
//        this.amazonS3ClientService.deleteFileFromS3Bucket(fileName);
//        Map<String, String> response = new HashMap<>();
//        response.put("message", "file [" + fileName + "] removing request submitted successfully.");
//
//        return response;
//    }
}
