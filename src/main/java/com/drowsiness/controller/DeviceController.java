package com.drowsiness.controller;

import com.drowsiness.dto.device.DeviceCreateDTO;
import com.drowsiness.dto.device.DeviceResponseDTO;
import com.drowsiness.dto.response.ApiResult;
import com.drowsiness.dto.response.SearchListResult;
import com.drowsiness.dto.response.SearchResult;
import com.drowsiness.dto.user.UserCreateDTO;
import com.drowsiness.dto.user.UserResponseDTO;
import com.drowsiness.dto.user.UserUpdateDTO;
import com.drowsiness.exception.ResourceNotFoundException;
import com.drowsiness.model.Device;
import com.drowsiness.model.User;
import com.drowsiness.service.DeviceService;
import com.drowsiness.service.FirmwareService;
import com.drowsiness.utils.StaticFuntion;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class DeviceController {
    private DeviceService deviceService;
    private ModelMapper modelMapper;
    private FirmwareService firmwareService;

    @Autowired
    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Autowired
    public void setFirmwareService(FirmwareService firmwareService) {
        this.firmwareService = firmwareService;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @GetMapping("/devices")
    public ResponseEntity<?> getAllDevices() {
        List<Device> deviceList = deviceService.findAllDevice();
        SearchListResult<?> result = new SearchListResult<>(deviceList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/devices/{deviceId}")
    public ResponseEntity<?> getDeviceById(@PathVariable UUID deviceId) {
        Optional<Device> searchDevice = deviceService.findDeviceById(deviceId);
        SearchResult<?> result = !searchDevice.equals(Optional.empty())
                ? new SearchResult<>(searchDevice.get()): new SearchResult<>();

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/devices/{deviceId}/firmwares/{firmwareId}")
    public ResponseEntity<?> setDeviceFirmware(@PathVariable UUID deviceId, @PathVariable UUID firmwareId) {
        Device device = deviceService.findDeviceByDeviceId(deviceId);
        device.setFirmware(firmwareService.findFirmwareById(firmwareId).get());
        deviceService.saveDevice(device);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResult<>(device,"Your device has been updated successfully"));
    }

    @PostMapping("/devices")
    public ResponseEntity<?> createDevice(@RequestBody DeviceCreateDTO deviceDTO) {

        if(deviceDTO.getSerialId() == null || deviceDTO.getSerialId().length() != 17) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SerialID is not valid!");
        } else if (deviceService.checkDuplicateSerialID(deviceDTO.getSerialId().toUpperCase()) == 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SerialID is duplicated!");
        } else {
            deviceDTO.setSerialId(deviceDTO.getSerialId().toUpperCase());
            Device reqDevice = modelMapper.map(deviceDTO, Device.class);
            reqDevice.setCreatedAt(StaticFuntion.getDate());
            reqDevice.setFirmware(firmwareService.findNewestFirmware());
            Device createdDevice = deviceService.saveDevice(reqDevice);

            DeviceResponseDTO deviceResponseDTO = modelMapper.map(createdDevice, DeviceResponseDTO.class);
            ApiResult<?> apiResult = new ApiResult<>(deviceResponseDTO,"Your device has been created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
        }
    }

    @PutMapping("/devices/{deviceId}/status")
    public ResponseEntity<?> setDeviceStatus(@PathVariable UUID deviceId, @RequestBody boolean isActive) {
        return deviceService.findDeviceById(deviceId).map(device -> {
            device.setActive(isActive);
            deviceService.saveDevice(device);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResult<>(device,"Your device has been updated successfully"));
        }).orElseThrow(() -> new ResourceNotFoundException("Device not found with id " + deviceId));
    }

    @PutMapping("/devices/{deviceId}")
    public ResponseEntity<?> updateDevice(@PathVariable UUID deviceId, @RequestBody DeviceCreateDTO deviceRequest) {
        if(deviceRequest.getSerialId() == null || deviceRequest.getSerialId().length() != 17) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SerialID is not valid!");
        } else if (deviceService.checkDuplicateSerialID(deviceRequest.getSerialId().toUpperCase(), deviceId) == 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SerialID is duplicated!");
        }

        return deviceService.findDeviceById(deviceId).map(device -> {
            device.setDeviceName(deviceRequest.getDeviceName());
            device.setUpdatedAt(StaticFuntion.getDate());
            device.setSerialId(deviceRequest.getSerialId().toUpperCase());
            device.setActive(deviceRequest.isActive());
            deviceService.saveDevice(device);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResult<>(device,"Your device has been updated successfully"));
        }).orElseThrow(() -> new ResourceNotFoundException("Device not found with id " + deviceId));
    }

    @DeleteMapping("/devices/{deviceId}")
    public ResponseEntity<?> removeDevice(@PathVariable UUID deviceId) {
        return deviceService.findDeviceById(deviceId).map(device -> {
            deviceService.removeDevice(device);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Device not found with id " + deviceId));
    }
}
