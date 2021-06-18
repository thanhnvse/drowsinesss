package com.drowsiness.controller;

import com.drowsiness.dto.datatracking.DataTrackingCreateDTO;
import com.drowsiness.dto.datatracking.DataTrackingResponseDTO;
import com.drowsiness.dto.datatracking.DataTrackingUpdateDTO;
import com.drowsiness.dto.device.DeviceResponseForDataTrackingDTO;
import com.drowsiness.dto.response.ApiResult;
import com.drowsiness.dto.response.SearchListResult;
import com.drowsiness.dto.response.SearchResult;
import com.drowsiness.dto.user.UserResponseForDataTrackingDTO;
import com.drowsiness.exception.ResourceNotFoundException;
import com.drowsiness.model.DataTracking;
import com.drowsiness.model.Device;
import com.drowsiness.model.User;
import com.drowsiness.service.DataTrackingService;
import com.drowsiness.service.DeviceService;
import com.drowsiness.service.UserDeviceService;
import com.drowsiness.service.UserService;
import com.drowsiness.utils.StaticFuntion;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class DataTrackingController {
    private DataTrackingService dataTrackingService;
    private UserService userService;
    private DeviceService deviceService;
    private UserDeviceService userDeviceService;
    private ModelMapper modelMapper;

    @Autowired
    public void setDataTrackingService(DataTrackingService dataTrackingService) {
        this.dataTrackingService = dataTrackingService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Autowired
    public void setUserDeviceService(UserDeviceService userDeviceService) {
        this.userDeviceService = userDeviceService;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @GetMapping("/data-trackings")
    public ResponseEntity<?> getAllDataTrackings() {
        List<DataTracking> dataTrackingList = dataTrackingService.findAllDataTracking();
        SearchListResult<?> result = new SearchListResult<>(dataTrackingList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/data-trackings/users/{userId}/devices/{deviceId}")
    public ResponseEntity<?> getAllDataTrackingsByUserDeviceId(@PathVariable UUID userId, @PathVariable UUID deviceId) {
        List<DataTracking> dataTrackingList = dataTrackingService.findByUserDeviceIdFromUserIdAndDeviceId(userId,deviceId);
        SearchListResult<?> result = new SearchListResult<>(dataTrackingList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/data-trackings/data-tracking-not-deleted")
    public ResponseEntity<?> getAllDataTrackingsNotDeleted() {
        List<DataTracking> dataTrackingList = dataTrackingService.findAllDataTrackingNotDeleted();
        SearchListResult<?> result = new SearchListResult<>(dataTrackingList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    //for admin
    @GetMapping("/devices/data-trackings")
    public ResponseEntity<?> getDataTrackingsByDevices() {
//        List<DeviceResponseForDataTrackingDTO> deviceResponseForDataTrackingDTOS  = new ArrayList<>();
//        List<DeviceResponseForDataTrackingDTO> deviceList = (List<DeviceResponseForDataTrackingDTO>)deviceService.findAllDevice().stream().map(device -> {
//
//            List<UserResponseForDataTrackingDTO> userResponseForDataTrackingDTOS = new ArrayList<>();
//            List<UserResponseForDataTrackingDTO> userList = (List<UserResponseForDataTrackingDTO>)
//                    userDeviceService.findUserByDeviceId(device.getDeviceId()).stream().map(user -> {
//                UserResponseForDataTrackingDTO userResponse = modelMapper.map(user,UserResponseForDataTrackingDTO.class);
//                userResponse.setDataTrackings(dataTrackingService.findByUserDeviceIdFromUserId(user.getUserId()));
//                userResponseForDataTrackingDTOS.add(userResponse);
//                return userResponseForDataTrackingDTOS.stream().collect(Collectors.toList());
//            });
//
//            DeviceResponseForDataTrackingDTO deviceResponseForDataTrackingDTO = modelMapper.map(device,DeviceResponseForDataTrackingDTO.class);
//            deviceResponseForDataTrackingDTO.setUserResponseForDataTrackingDTOList(userList);
//            deviceResponseForDataTrackingDTOS.add(deviceResponseForDataTrackingDTO);
//            return deviceResponseForDataTrackingDTOS.stream().collect(Collectors.toList());
//        });
        List<DeviceResponseForDataTrackingDTO> deviceList = new ArrayList<>();
        List<UserResponseForDataTrackingDTO> user1List = new ArrayList<>();
        List<Device> devices = deviceService.findAllDevice();
        for(Device device : devices){
            DeviceResponseForDataTrackingDTO deviceResponse = new DeviceResponseForDataTrackingDTO();
            deviceResponse.setDeviceId(device.getDeviceId());
            deviceResponse.setDeviceName(device.getDeviceName());
            deviceResponse.setCreatedAt(device.getCreatedAt());
            deviceResponse.setUpdatedAt(device.getUpdatedAt());
            UserResponseForDataTrackingDTO userResponseForDataTrackingDTO = new UserResponseForDataTrackingDTO();
            List<User> userList = userDeviceService.findUserByDeviceId(device.getDeviceId());
            for(User user : userList){
                userResponseForDataTrackingDTO.setUserId(user.getUserId());
                userResponseForDataTrackingDTO.setFullName(user.getFullName());
                userResponseForDataTrackingDTO.setUsername(user.getUsername());
                userResponseForDataTrackingDTO.setPhoneNumber(user.getPhoneNumber());
                userResponseForDataTrackingDTO.setActive(user.isActive());
                userResponseForDataTrackingDTO.setAvatar(user.getAvatar());
                userResponseForDataTrackingDTO.setCreatedAt(user.getCreatedAt());
                userResponseForDataTrackingDTO.setUpdatedAt(user.getUpdatedAt());
                userResponseForDataTrackingDTO.setEmail(user.getEmail());
                userResponseForDataTrackingDTO.setDataTrackings(dataTrackingService.findByUserDeviceIdFromUserId(user.getUserId()));
                user1List.add(userResponseForDataTrackingDTO);
            }
            deviceResponse.setUserResponseForDataTrackingDTOList(user1List);
            deviceList.add(deviceResponse);
        }
        SearchListResult<?> result = new SearchListResult<>(deviceList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/data-trackings/{dataTrackingId}")
    public ResponseEntity<?> getDataTrackingById(@PathVariable UUID dataTrackingId) {
        Optional<DataTracking> searchDataTracking = dataTrackingService.findDataTrackingById(dataTrackingId);
        SearchResult<?> result = !searchDataTracking.equals(Optional.empty())
                ? new SearchResult<>(searchDataTracking.get()): new SearchResult<>();

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/data-trackings")
    public ResponseEntity<?> createDataTracking(@RequestBody DataTrackingCreateDTO dataTrackingCreateDTO) {
        DataTracking dataTracking = new DataTracking();
        dataTracking.setUserDevice(userDeviceService.findByUserIdAndDeviceId
                (dataTrackingCreateDTO.getUserId(),dataTrackingCreateDTO.getDeviceId()));
        dataTracking.setImageUrl(dataTrackingCreateDTO.getImageUrl());
        dataTracking.setTrackingAt(StaticFuntion.getDate());
        dataTracking.setDeleted(false);

        dataTrackingService.saveDataTracking(dataTracking);

        DataTrackingResponseDTO dataTrackingResponseDTO = new DataTrackingResponseDTO();
        dataTrackingResponseDTO.setUser(userService.findUserByUserId(dataTrackingCreateDTO.getUserId()));
        dataTrackingResponseDTO.setDevice(deviceService.findDeviceByDeviceId(dataTrackingCreateDTO.getDeviceId()));

        ApiResult<?> apiResult = new ApiResult<>(dataTrackingResponseDTO,"Your data tracking has been created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
    }

    @PutMapping("/data-trackings/{dataTrackingId}")
    public ResponseEntity<?> updateDataTracking(@PathVariable UUID dataTrackingId, @RequestBody DataTrackingUpdateDTO dataTrackingRequest) {
        return dataTrackingService.findDataTrackingById(dataTrackingService
                .findDataTrackingById(dataTrackingId).get().getDataTrackingId()).map(dataTracking -> {
            dataTracking.setDeleted(dataTrackingRequest.isDeleted());
            dataTracking.setTrackingAt(StaticFuntion.getDate());
            dataTrackingService.saveDataTracking(dataTracking);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResult<>(dataTrackingRequest,"Your data tracking has been updated successfully"));
        }).orElseThrow(() -> new ResourceNotFoundException("Data tracking not found with data tracking id " + dataTrackingId ));
    }
}