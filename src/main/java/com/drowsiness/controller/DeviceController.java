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
import com.drowsiness.utils.StaticFuntion;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class DeviceController {
    private DeviceService deviceService;
    private ModelMapper modelMapper;

    @Autowired
    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
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

    @PostMapping("/devices")
    public ResponseEntity<?> createDevice(@RequestBody DeviceCreateDTO deviceDTO) {
        Device reqDevice = modelMapper.map(deviceDTO, Device.class);
//        reqUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        reqDevice.setCreatedAt(StaticFuntion.getDate());
        Device createdDevice = deviceService.saveDevice(reqDevice);

        DeviceResponseDTO deviceResponseDTO = modelMapper.map(createdDevice, DeviceResponseDTO.class);
        ApiResult<?> apiResult = new ApiResult<>(deviceResponseDTO,"Your device has been created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
    }

    @PutMapping("/devices/{deviceId}")
    public ResponseEntity<?> updateDevice(@PathVariable UUID deviceId, @RequestBody DeviceCreateDTO deviceRequest) {
        return deviceService.findDeviceById(deviceId).map(device -> {
            device.setDeviceName(deviceRequest.getDeviceName());
            device.setUpdatedAt(StaticFuntion.getDate());
            deviceService.saveDevice(device);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResult<>(deviceRequest,"Your device has been updated successfully"));
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
