package com.drowsiness.controller;

import com.drowsiness.dto.device.DeviceResponseDTO;
import com.drowsiness.dto.response.ApiResult;
import com.drowsiness.dto.response.SearchListResult;
import com.drowsiness.dto.response.SearchResult;
import com.drowsiness.dto.userdevice.*;
import com.drowsiness.exception.ResourceNotFoundException;
import com.drowsiness.model.User;
import com.drowsiness.model.UserDevice;
import com.drowsiness.service.DeviceService;
import com.drowsiness.service.UserDeviceService;
import com.drowsiness.service.UserService;
import com.drowsiness.utils.StaticFuntion;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class UserDeviceController {
    private UserDeviceService userDeviceService;
    private UserService  userService;
    private DeviceService deviceService;
    private ModelMapper modelMapper;

    @Autowired
    public void setUserDeviceService(UserDeviceService userDeviceService) {
        this.userDeviceService = userDeviceService;
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
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @GetMapping("/user-devices")
    public ResponseEntity<?> getAllUserDevices() {
        List<UserDevice> userDeviceList = userDeviceService.findAllUserDevice();
        SearchListResult<?> result = new SearchListResult<>(userDeviceList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/user-devices/{userId}/user")
    public ResponseEntity<?> getUserInDeviceConnectedByUserId(@PathVariable UUID userId) {
        User user = userDeviceService.findUserByUserDeviceConnected(userId);
        SearchResult<?> result = !user.equals(Optional.empty())
                ? new SearchResult<>(user) : new SearchResult<>();

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/user-devices/{userId}/userdevice")
    public ResponseEntity<?> getUserDeviceConnectedByUserId(@PathVariable UUID userId) {
        UserDevice userdevice = userDeviceService.findByUserIdAndAndConnected(userId);
        DeviceResponseDTO dto = new DeviceResponseDTO();
        dto.setDeviceName(userdevice.getDevice().getDeviceName());
        dto.setCreatedAt(userdevice.getConnectedAt());
        dto.setUpdatedAt(userdevice.getUpdatedAt());
        dto.setFirmware(userdevice.getDevice().getFirmware());
        dto.setDeviceId(userdevice.getDevice().getDeviceId());
        SearchResult<?> result = !dto.equals(Optional.empty())
                ? new SearchResult<>(dto) : new SearchResult<>();

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/user-devices/{userId}/user/history")
    public ResponseEntity<?> getUserHistoryConnected(@PathVariable UUID userId) {
        List<UserDevice> userDeviceList = userDeviceService.findAllDeviceConnectedByUserId(userId);
        List<UserDeviceHistoryDTO> userDeviceHistoryDTOS = new ArrayList<>();
        for(UserDevice userDevice : userDeviceList){
            UserDeviceHistoryDTO userDeviceHistoryDTO = new UserDeviceHistoryDTO();
            userDeviceHistoryDTO.setDeviceId(userDevice.getDevice().getDeviceId());
            userDeviceHistoryDTO.setDeviceName(userDevice.getDevice().getDeviceName());
            userDeviceHistoryDTO.setCreatedAt(userDevice.getDevice().getCreatedAt());
            userDeviceHistoryDTO.setConnected(userDevice.isConnected());
            userDeviceHistoryDTO.setConnectedAt(userDevice.getConnectedAt());
            userDeviceHistoryDTO.setDisconnectedAt(userDevice.getDisconnectedAt());
            userDeviceHistoryDTOS.add(userDeviceHistoryDTO);
        }
        SearchListResult<?> result = new SearchListResult<>(userDeviceHistoryDTOS);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/user-devices/{userId}/user/{deviceId}/device")
    public ResponseEntity<?> getInformationUserAndDeviceConnected(@PathVariable UUID userId, @PathVariable UUID deviceId) {
        UserDevice userDevice = userDeviceService.findByUserIdAndDeviceId(userId, deviceId);
        UserDeviceResponseDTO userDeviceResponseDTO = modelMapper.map(userDevice,UserDeviceResponseDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(userDeviceResponseDTO);
    }

    @PostMapping("/user-devices")
    public ResponseEntity<?> connectUserInDevice(@RequestBody UserDeviceCreateDTO userDeviceDTO) {
//        UserDevice reqUserDevice = modelMapper.map(userDeviceDTO, UserDevice.class);

        //withdraw the user is connecting the device
        List<UserDevice> listUD = userDeviceService.findByDeviceID(userDeviceDTO.getDeviceId());
        if(listUD != null){
            for (UserDevice ud : listUD) {
                ud.setConnected(false);
                userDeviceService.saveUserDevice(ud);
            }
        }

        //check the last connected user in device
        //withdraw other device user connected in
        listUD = userDeviceService.findByUserId(userDeviceDTO.getUserId());
        if(listUD != null){

            for (UserDevice ud : listUD) {
                ud.setConnected(false);
                userDeviceService.saveUserDevice(ud);
            }
        }

        //check existed
        if(userDeviceService.checkExistedUser(userDeviceDTO.getUserId(),userDeviceDTO.getDeviceId())){
            UserDevice existedUser = userDeviceService.findByUserIdAndDeviceId(userDeviceDTO.getUserId(),userDeviceDTO.getDeviceId());
            existedUser.setConnected(true);
            existedUser.setConnectedAt(StaticFuntion.getDate());
            UserDevice updateConnectedUserDevice = userDeviceService.saveUserDevice(existedUser);
            UserDeviceConnectedResponseDTO userDeviceUpdateResponseDTO = modelMapper.map(updateConnectedUserDevice, UserDeviceConnectedResponseDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResult<>(userDeviceUpdateResponseDTO,"Your acccount has been connected with device"));
        }

        //connect with new user
        UserDevice reqUserDevice = new UserDevice();

        reqUserDevice.setAccountUser(userService.findUserByUserId(userDeviceDTO.getUserId()));
        reqUserDevice.setDevice(deviceService.findDeviceByDeviceId(userDeviceDTO.getDeviceId()));

        reqUserDevice.setConnectedAt(StaticFuntion.getDate());
        reqUserDevice.setConnected(true);

        UserDevice createdUserDevice = userDeviceService.saveUserDevice(reqUserDevice);

        UserDeviceConnectedResponseDTO userDeviceResponseDTO = modelMapper.map(createdUserDevice, UserDeviceConnectedResponseDTO.class);
        ApiResult<?> apiResult = new ApiResult<>(userDeviceResponseDTO,"Your account has been connected with device successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
    }

    @PutMapping("/user-devices/user/{userId}/device/{deviceId}/connect")
    public ResponseEntity<?> setConnectionUserInDevice(@PathVariable UUID userId, @PathVariable UUID deviceId, @RequestBody UserDeviceUpdateDTO userDeviceRequest) {
        UUID userDeviceId = userDeviceService.findByUserIdAndDeviceId(userId,deviceId).getUserDeviceId();
        return userDeviceService.findUserDeviceById(userDeviceId).map(userDevice -> {
            userDevice.setConnected(userDeviceRequest.isConnected());
            userDevice.setUpdatedAt(StaticFuntion.getDate());
            if (userDeviceRequest.isConnected()) {
                userDevice.setConnectedAt(StaticFuntion.getDate());
            } else {
                userDevice.setDisconnectedAt(StaticFuntion.getDate());
            }
            userDeviceService.saveUserDevice(userDevice);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResult<>(userDeviceRequest,"Your account has been updated the connection successfully"));
        }).orElseThrow(() -> new ResourceNotFoundException("UserDevice not found with id " + userDeviceId));
    }
}
