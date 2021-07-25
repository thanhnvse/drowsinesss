package com.drowsiness.controller;

import com.drowsiness.dto.response.ApiResult;
import com.drowsiness.dto.user.UserCreateDTO;
import com.drowsiness.dto.user.UserLoginByGGDTO;
import com.drowsiness.dto.user.UserLoginDTO;
import com.drowsiness.dto.user.UserResponseDTO;
import com.drowsiness.filter.JwtProvider;
import com.drowsiness.filter.JwtResponse;
import com.drowsiness.model.User;
import com.drowsiness.repository.RoleRepository;
import com.drowsiness.repository.UserRepository;
import com.drowsiness.service.RoleService;
import com.drowsiness.service.UserService;
import com.drowsiness.utils.StaticFuntion;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    AuthenticationManager authenticationManager;

    JwtProvider jwtProvider;

    private UserRepository userRepository;

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    private RoleRepository roleRepository;

    PasswordEncoder encoder;

    ModelMapper modelMapper;

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setJwtProvider(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userDTO) {
        User res = userService.findUserByUsername(userDTO.getUsername());
        User createdUser = userService.login(userDTO.getUsername(), res.getPassword());
        if(createdUser == null){
            //wrong password
            if(userService.findUserByUsername(userDTO.getUsername()) != null){
                ApiResult<?> apiResult = new ApiResult<>("Your password account is incorrect");
                return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
            }
            //no value in database
            ApiResult<?> apiResult = new ApiResult<>("Your account doesn't sign up before");
            return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
        }
        //inactive
        if(createdUser != null && !createdUser.isActive()){
            ApiResult<?> apiResult = new ApiResult<>("Your account has inactivate status");
            return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
        }
        //success
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDTO.getUsername(),
                        userDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);

        JwtResponse jwtResponse = new JwtResponse(res.getUserId(), res.getUsername(), res.getFullName(),
                res.getPassword(), res.getPhoneNumber(), res.getEmail(), res.getAvatar(),
        res.isActive(), res.getCreatedAt(), res.getUpdatedAt(), jwt);

        ApiResult<?> apiResult = new ApiResult<>(jwtResponse,"Your account has been signed in successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
    }

    @PostMapping("/signup")
    public ResponseEntity registerUser(@RequestBody UserCreateDTO userDTO) {
        //check existed username in db
        if(userRepository.findByUsername(userDTO.getUsername()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already taken!");
        }

        // Creating user's account
        User reqUser = modelMapper.map(userDTO, User.class);
        reqUser.setPassword(encoder.encode(userDTO.getPassword()));
        reqUser.setActive(true);
        reqUser.setCreatedAt(StaticFuntion.getDate());
        reqUser.setPhoneNumber(userDTO.getPhoneNumber());
        reqUser.setRole(roleService.findRoleByRoleName("ROLE_USER"));
        User createdUser = userService.saveUser(reqUser);

        UserResponseDTO userResponseDTO = modelMapper.map(createdUser, UserResponseDTO.class);
        ApiResult<?> apiResult = new ApiResult<>(userResponseDTO,"Your account has been registered successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
    }

    @PostMapping("/login-mail")
    public ResponseEntity loginByGG(@RequestBody UserLoginByGGDTO userDTO) {
        //check existed email login
        User checkUser = userService.findUserByEmail(userDTO.getEmail());
        User reqUser = new User();
        if(checkUser == null){
            //create new user with username is email
            reqUser.setUsername(userDTO.getEmail());
            reqUser.setPassword(encoder.encode(userDTO.getEmail()));
            reqUser.setFullName(userDTO.getFullName());
            reqUser.setAvatar(userDTO.getAvatar());
            reqUser.setEmail(userDTO.getEmail());
            reqUser.setActive(true);
            reqUser.setCreatedAt(StaticFuntion.getDate());
            reqUser.setPhoneNumber(userDTO.getPhoneNumber());
            reqUser.setRole(roleService.findRoleByRoleName("ROLE_USER"));
            //save
            userService.saveUser(reqUser);
        }else{
            reqUser = new User(checkUser.getUserId(), checkUser.getUsername(), checkUser.getFullName(), checkUser.getPassword(),
                    checkUser.getPhoneNumber(), checkUser.getEmail(), checkUser.getAvatar(), checkUser.isActive(),
                    checkUser.getCreatedAt(), checkUser.getUpdatedAt());
        }
        //username, password is email
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        reqUser.getUsername(),
                        reqUser.getEmail()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);

        JwtResponse jwtResponse = new JwtResponse(reqUser.getUserId(), reqUser.getUsername(), reqUser.getFullName(),
                reqUser.getPassword(), reqUser.getPhoneNumber(), reqUser.getEmail(), reqUser.getAvatar(),
                reqUser.isActive(), reqUser.getCreatedAt(), reqUser.getUpdatedAt(), jwt);

        ApiResult<?> apiResult = new ApiResult<>(jwtResponse,"Your account has been signed in successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);

    }
}
