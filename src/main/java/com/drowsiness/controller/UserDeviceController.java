package com.drowsiness.controller;

import com.drowsiness.dto.response.ApiResult;
import com.drowsiness.dto.response.SearchListResult;
import com.drowsiness.dto.response.SearchResult;
import com.drowsiness.dto.user.UserCreateDTO;
import com.drowsiness.dto.user.UserResponseDTO;
import com.drowsiness.dto.user.UserUpdateDTO;
import com.drowsiness.dto.userdevice.UserDeviceCreateDTO;
import com.drowsiness.dto.userdevice.UserDeviceUpdateDTO;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @GetMapping("/use-devices")
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

    @PostMapping("/user-devices")
    public ResponseEntity<?> connectUserInDevice(@RequestBody UserDeviceCreateDTO userDeviceDTO) {
//        UserDevice reqUserDevice = modelMapper.map(userDeviceDTO, UserDevice.class);
        UserDevice userDevice = userDeviceService.findDeviceByUserDeviceConnected(userDeviceDTO.getDeviceId());
        if(userDevice != null){
            userDevice.setConnected(false);
            userDeviceService.saveUserDevice(userDevice);
        }

        UserDevice reqUserDevice = new UserDevice();

        reqUserDevice.setAccountUser(userService.findUserByUserId(userDeviceDTO.getUserId()));
        reqUserDevice.setDevice(deviceService.findDeviceByDeviceId(userDeviceDTO.getDeviceId()));

        reqUserDevice.setConnectedAt(StaticFuntion.getDate());
        reqUserDevice.setConnected(true);

        UserDevice createdUserDevice = userDeviceService.saveUserDevice(reqUserDevice);

        UserResponseDTO userResponseDTO = modelMapper.map(createdUserDevice, UserResponseDTO.class);
        ApiResult<?> apiResult = new ApiResult<>(userResponseDTO,"Your account has been connected with device successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
    }

    @PutMapping("/user-devices/user/{userId}/device/{deviceId}/connect")
    public ResponseEntity<?> setConnectionUserInDevice(@PathVariable UUID userId, @PathVariable UUID deviceId, @RequestBody UserDeviceUpdateDTO userDeviceRequest) {
        UUID userDeviceId = userDeviceService.findByUserIdAndDeviceId(userId,deviceId).getUserDeviceId();
        return userDeviceService.findUserDeviceById(userDeviceId).map(userDevice -> {
            userDevice.setConnected(userDeviceRequest.isConnected());
            userDevice.setUpdatedAt(StaticFuntion.getDate());
            userDeviceService.saveUserDevice(userDevice);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResult<>(userDeviceRequest,"Your account has been updated the connection successfully"));
        }).orElseThrow(() -> new ResourceNotFoundException("UserDevice not found with id " + userDeviceId));
    }
}
