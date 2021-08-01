package com.drowsiness.controller;

import com.drowsiness.dto.device.DeviceCreateDTO;
import com.drowsiness.dto.firmware.FirmwareDTO;
import com.drowsiness.dto.response.ApiResult;
import com.drowsiness.dto.response.SearchListResult;
import com.drowsiness.dto.response.SearchResult;
import com.drowsiness.exception.ResourceNotFoundException;
import com.drowsiness.model.Firmware;
import com.drowsiness.service.FileService;
import com.drowsiness.service.FirmwareService;
import com.drowsiness.utils.StaticFuntion;
import com.google.firebase.auth.FirebaseAuthException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping({"/api/v1"})
public class FirmwareController {
    private FirmwareService fwService;
    private ModelMapper modelMapper;
    private FileService fileService;

    @Autowired
    public void setFirmwareService(FirmwareService fwService) {
        this.fwService = fwService;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping({"/firmwares"})
    public ResponseEntity<?> getAllFirmwares() {
        List<Firmware> fwList = this.fwService.findAllFirmware();
        SearchListResult<?> result = new SearchListResult(fwList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping({"/firmwares/newest"})
    public ResponseEntity<?> getNewestFirmware() {
        Firmware fw = this.fwService.findNewestFirmware();
        SearchResult<?> result = fw != null ? new SearchResult(fw) : new SearchResult();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping({"/firmwares"})
    public ResponseEntity<?> createFirmware(@ModelAttribute FirmwareDTO fwDTO) throws InterruptedException, ExecutionException, IOException, FirebaseAuthException {
        Firmware fw = new Firmware();
        fw.setTimeDetection(fwDTO.getTimeDetection());
        fw.setDescription(fwDTO.getDescription());
        fw.setModelUrl(this.fileService.getZipUrl(fwDTO.getFile()));
        fw.setCreatedAt(StaticFuntion.getDate());
        fw.setUpdatedAt(StaticFuntion.getDate());
        fw.setActive(false);
        this.fwService.saveFirmware(fw);
        //this.fwService.deactivateFirmwaresExceptThis(fw.getFirmwareId());
        ApiResult<?> apiResult = new ApiResult(fw, "New firmware has been created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
    }

    @PutMapping("/firmwares/{firmwareId}")
    public ResponseEntity<?> activeFirmwareStatus(@PathVariable UUID firmwareId) {
        Firmware fw = this.fwService.findFirmwareById(firmwareId).get();
        fw.setActive(true);
        this.fwService.saveFirmware(fw);
        this.fwService.deactivateFirmwaresExceptThis(fw.getFirmwareId());
        ApiResult<?> apiResult = new ApiResult(fw, "The firmware is activated!! Other firmwares are deactivated!!");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
    }

    @PutMapping("/firmwares/{firmwareId}/times")
    public ResponseEntity<?> updateFirmwareDetectionTime(@PathVariable UUID firmwareId, @RequestParam("timeDetection") float timeDetection) {
        return this.fwService.findFirmwareById(firmwareId).map(fw -> {
            if(timeDetection < 1 || timeDetection > 3)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time Detection is not valid! (From 1 - 3)");
            fw.setTimeDetection(timeDetection);
            fw.setUpdatedAt(StaticFuntion.getDate());
            this.fwService.saveFirmware(fw);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResult<>(fw,"Firmware " + firmwareId +" has been updated successfully"));
        }).orElseThrow(() -> new ResourceNotFoundException("Firmware not found with id " + firmwareId));
    }
}
