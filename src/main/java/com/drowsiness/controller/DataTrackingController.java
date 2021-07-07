package com.drowsiness.controller;

import com.drowsiness.dto.datatracking.*;
import com.drowsiness.dto.device.DeviceResponseForDataTrackingByDeviceDTO;
import com.drowsiness.dto.device.DeviceResponseForDataTrackingDTO;
import com.drowsiness.dto.response.ApiResult;
import com.drowsiness.dto.response.SearchListResult;
import com.drowsiness.dto.response.SearchResult;
import com.drowsiness.dto.user.UserResponseForDataTrackingDTO;
import com.drowsiness.dto.userdevice.UserDeviceHistoryDTO;
import com.drowsiness.exception.ResourceNotFoundException;
import com.drowsiness.model.DataTracking;
import com.drowsiness.model.Device;
import com.drowsiness.model.User;
import com.drowsiness.model.UserDevice;
import com.drowsiness.service.*;
import com.drowsiness.utils.StaticFuntion;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1")
public class DataTrackingController {
    private DataTrackingService dataTrackingService;
    private UserService userService;
    private DeviceService deviceService;
    private UserDeviceService userDeviceService;
    private FileService fileService;
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
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @GetMapping("/data-trackings")
    public ResponseEntity<?> getAllDataTrackings() {
        List<DataTrackingResponseForUserAndDeviceDTO> dataTrackingResponseForUserAndDeviceDTOS = new ArrayList<>();
        List<DataTracking> dataTrackingList = dataTrackingService.findAllDataTracking();
        for(DataTracking dataTracking : dataTrackingList){
            DataTrackingResponseForUserAndDeviceDTO dataTrackingResponseForUserAndDeviceDTO = modelMapper.map(dataTracking,DataTrackingResponseForUserAndDeviceDTO.class);
            dataTrackingResponseForUserAndDeviceDTOS.add(dataTrackingResponseForUserAndDeviceDTO);
        }
        SearchListResult<?> result = new SearchListResult<>(dataTrackingResponseForUserAndDeviceDTOS);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/data-trackings-infos")
    public ResponseEntity<?> getAllDataTrackingsWithInfo() {
        List<DataTrackingFullDTO> resultList = new ArrayList<>();
        List<UserDevice> udList = userDeviceService.findAllUserDevice();
        DataTrackingFullDTO dto;
        for(UserDevice ud : udList){
            for (DataTracking dt : ud.getDataTrackings()) {
                dto = new DataTrackingFullDTO();
                dto.setDataTrackingId(dt.getDataTrackingId());
                dto.setTrackingAt(dt.getTrackingAt());
                dto.setDeleted(dt.isDeleted());
                dto.setImageUrl(dt.getImageUrl());

                dto.setUserDeviceId(ud.getUserDeviceId());
                dto.setUserId(ud.getAccountUser().getUserId());
                dto.setDeviceId(ud.getDevice().getDeviceId());

                resultList.add(dto);
            }
        }
        SearchListResult<?> result = new SearchListResult<>(resultList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/data-trackings/users/{userId}/devices/{deviceId}")
    public ResponseEntity<?> getAllDataTrackingsByUserDeviceId(@PathVariable UUID userId, @PathVariable UUID deviceId) {
        List<DataTracking> dataTrackingList = dataTrackingService.findByUserDeviceIdFromUserIdAndDeviceId(userId,deviceId);
        List<DataTrackingResponseForUserAndDeviceDTO> dataTrackingResponseForUserAndDeviceDTOList = new ArrayList<>();
        for(DataTracking dto : dataTrackingList){
            DataTrackingResponseForUserAndDeviceDTO dataTrackingResponse = modelMapper.map(dto,DataTrackingResponseForUserAndDeviceDTO.class);
            dataTrackingResponseForUserAndDeviceDTOList.add(dataTrackingResponse);
        }
        SearchListResult<?> result = new SearchListResult<>(dataTrackingResponseForUserAndDeviceDTOList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/data-trackings/users/{userId}/devices/{deviceId}/connected")
    public ResponseEntity<?> getAllDataTrackingsByUserDeviceIdConnected(@PathVariable UUID userId, @PathVariable UUID deviceId) {
        List<DataTracking> dataTrackingList = dataTrackingService.findByUserDeviceIdFromUserIdAndDeviceIdConnected(userId,deviceId);
        List<DataTrackingResponseForUserAndDeviceDTO> dataTrackingResponseList = new ArrayList<>();
        for(DataTracking dto : dataTrackingList){
            DataTrackingResponseForUserAndDeviceDTO dataTrackingResponse = modelMapper.map(dto,DataTrackingResponseForUserAndDeviceDTO.class);
            dataTrackingResponseList.add(dataTrackingResponse);
        }
        SearchListResult<?> result = new SearchListResult<>(dataTrackingResponseList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/data-trackings/data-tracking-not-deleted")
    public ResponseEntity<?> getAllDataTrackingsNotDeleted() {
        List<DataTracking> dataTrackingList = dataTrackingService.findAllDataTrackingNotDeleted();
        SearchListResult<?> result = new SearchListResult<>(dataTrackingList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    //for admin
    @GetMapping("/devices/users/data-trackings")
    public ResponseEntity<?> getDataTrackingsByDevices() {
        List<DeviceResponseForDataTrackingDTO> deviceList = new ArrayList<>();
        List<Device> devices = deviceService.findAllDevice();
        for(Device device : devices){
            DeviceResponseForDataTrackingDTO deviceResponse = new DeviceResponseForDataTrackingDTO();
            deviceResponse.setDeviceId(device.getDeviceId());
            deviceResponse.setDeviceName(device.getDeviceName());
            deviceResponse.setCreatedAt(device.getCreatedAt());
            deviceResponse.setUpdatedAt(device.getUpdatedAt());

            List<User> userList = userDeviceService.findUserByDeviceId(device.getDeviceId());
            List<UserResponseForDataTrackingDTO> user1List = new ArrayList<>();
            for(User user : userList){
                UserResponseForDataTrackingDTO userResponseForDataTrackingDTO = new UserResponseForDataTrackingDTO();
                userResponseForDataTrackingDTO.setUserId(user.getUserId());
                userResponseForDataTrackingDTO.setFullName(user.getFullName());
                userResponseForDataTrackingDTO.setUsername(user.getUsername());
                userResponseForDataTrackingDTO.setPhoneNumber(user.getPhoneNumber());
                userResponseForDataTrackingDTO.setActive(user.isActive());
                userResponseForDataTrackingDTO.setAvatar(user.getAvatar());
                userResponseForDataTrackingDTO.setCreatedAt(user.getCreatedAt());
                userResponseForDataTrackingDTO.setUpdatedAt(user.getUpdatedAt());
                userResponseForDataTrackingDTO.setEmail(user.getEmail());

                List<DataTracking> dataTrackings = dataTrackingService.findByUserDeviceIdFromUserId(user.getUserId());
                List<DataTrackingResponseForUserAndDeviceDTO> dataTrackingList = new ArrayList<>();
                for(DataTracking dataTracking : dataTrackings){
                    DataTrackingResponseForUserAndDeviceDTO dataTrackingResponse = new DataTrackingResponseForUserAndDeviceDTO();
                    dataTrackingResponse.setDataTrackingId(dataTracking.getDataTrackingId());
                    dataTrackingResponse.setTrackingAt(dataTracking.getTrackingAt());
                    dataTrackingResponse.setDeleted(dataTracking.isDeleted());
                    dataTrackingResponse.setImageUrl(dataTracking.getImageUrl());
                    dataTrackingList.add(dataTrackingResponse);
                }
                userResponseForDataTrackingDTO.setDataTrackings(dataTrackingList);
                user1List.add(userResponseForDataTrackingDTO);
            }
            deviceResponse.setUsers(user1List);
            deviceList.add(deviceResponse);
        }
        SearchListResult<?> result = new SearchListResult<>(deviceList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/data-trackings/{dataTrackingId}")
    public ResponseEntity<?> getDataTrackingById(@PathVariable UUID dataTrackingId) {
        Optional<DataTracking> searchDataTracking = dataTrackingService.findDataTrackingById(dataTrackingId);
        SearchResult<?> result = new SearchResult<>();
        if(!searchDataTracking.equals(Optional.empty())){
            DataTrackingResponseWithDevice dataTrackingResponseWithDevice = new DataTrackingResponseWithDevice();
            dataTrackingResponseWithDevice.setDataTrackingId(dataTrackingId);
            dataTrackingResponseWithDevice.setDeleted(searchDataTracking.get().isDeleted());
            dataTrackingResponseWithDevice.setImageUrl(searchDataTracking.get().getImageUrl());
            dataTrackingResponseWithDevice.setTrackingAt(searchDataTracking.get().getTrackingAt());
            dataTrackingResponseWithDevice.setDeviceId(dataTrackingService.findDataTrackingByDataTrackingId(dataTrackingId).getUserDevice().getDevice().getDeviceId());
            result = new SearchResult<>(dataTrackingResponseWithDevice);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/data-trackings/user/{userId}")
    public ResponseEntity<?> getByUserId(@PathVariable UUID userId) {
        List<DataTracking> dataTrackingList = dataTrackingService.findByUserId(userId);
        List<DataTrackingResponseWithDevice> dataTrackings = new ArrayList<>();
        for(DataTracking dto : dataTrackingList){
            DataTrackingResponseWithDevice d = new DataTrackingResponseWithDevice();
            d.setDataTrackingId(dto.getDataTrackingId());
            d.setDeleted(dto.isDeleted());
            d.setImageUrl(dto.getImageUrl());
            d.setTrackingAt(dto.getTrackingAt());
            d.setDeviceId(dto.getUserDevice().getDevice().getDeviceId());
            d.setDeviceName(dto.getUserDevice().getDevice().getDeviceName());

            dataTrackings.add(d);
        }
        SearchListResult<?> result = new SearchListResult<>(dataTrackings);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/data-trackings/users/{userId}")
    public ResponseEntity<?> getDataTrackingByUserId(@PathVariable UUID userId) {

        List<Device> searchDevices = deviceService.findAllDeviceByUserId(userId);
        if(searchDevices.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(new SearchResult<>());
        }
        List<DataTrackingResponseByDeviceDTO> dataList = new ArrayList<>();
        for(Device device: searchDevices){
            DeviceResponseForDataTrackingByDeviceDTO deviceDTO = modelMapper.map(device,DeviceResponseForDataTrackingByDeviceDTO.class);
            List<DataTracking> dataTrackings = dataTrackingService.findByUserDeviceIdFromUserIdAndDeviceId(userId,device.getDeviceId());
            List<DataTrackingResponseWithDevice> dataTrackingResponseWithDevices = new ArrayList<>();
            for(DataTracking dataTracking : dataTrackings){
                DataTrackingResponseWithDevice dataTrackingResponseWithDevice = new DataTrackingResponseWithDevice();
                dataTrackingResponseWithDevice.setDataTrackingId(dataTracking.getDataTrackingId());
                dataTrackingResponseWithDevice.setTrackingAt(dataTracking.getTrackingAt());
                dataTrackingResponseWithDevice.setImageUrl(dataTracking.getImageUrl());
                dataTrackingResponseWithDevice.setDeleted(dataTracking.isDeleted());
                dataTrackingResponseWithDevices.add(dataTrackingResponseWithDevice);
            }
            DataTrackingResponseByDeviceDTO dataTrackingResponseByDeviceDTO = new DataTrackingResponseByDeviceDTO(deviceDTO,dataTrackingResponseWithDevices);
            dataList.add(dataTrackingResponseByDeviceDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(dataList);
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

    @PostMapping("/data-trackings/users/devices/image")
    public ResponseEntity<?> createDataTrackingWithImage(@ModelAttribute DataTrackingCreateImageDTO dataTrackingCreateDTO) throws InterruptedException, ExecutionException, IOException {
        DataTracking dataTracking = new DataTracking();
        dataTracking.setUserDevice(userDeviceService.findByUserIdAndDeviceId
                (dataTrackingCreateDTO.getUserId(),dataTrackingCreateDTO.getDeviceId()));
        dataTracking.setImageUrl(fileService.getImgUrl(dataTrackingCreateDTO.getFile()));
        if(dataTrackingCreateDTO.getTrackingAt() == null){
            dataTracking.setTrackingAt(StaticFuntion.getDate());
        }else{
            dataTracking.setTrackingAt(dataTrackingCreateDTO.getTrackingAt());
        }
        dataTracking.setDeleted(false);

        dataTrackingService.saveDataTracking(dataTracking);

        DataTrackingResponseDTO dataTrackingResponseDTO = new DataTrackingResponseDTO();
        dataTrackingResponseDTO.setUser(userService.findUserByUserId(dataTrackingCreateDTO.getUserId()));
        dataTrackingResponseDTO.setDevice(deviceService.findDeviceByDeviceId(dataTrackingCreateDTO.getDeviceId()));

        ApiResult<?> apiResult = new ApiResult<>(dataTrackingResponseDTO,"Your data tracking has been created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
    }

    //@PostMapping("/data-trackings/pic")
    public Object upload(@RequestParam("file") MultipartFile multipartFile) throws Exception{
        return fileService.upload(multipartFile);
    }

    //@PostMapping(value = "/data-trackings/pic/{fileName:.+}")
    public Object download(@PathVariable String fileName) throws IOException {
        return fileService.download(fileName);
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
