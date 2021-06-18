package com.drowsiness.filter;

import com.drowsiness.dto.response.ApiResult;
import com.drowsiness.dto.user.UserCreateDTO;
import com.drowsiness.dto.user.UserResponseDTO;
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
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    AuthenticationManager authenticationManager;

    JwtProvider jwtProvider;

    UserRepository userRepository;

    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    RoleRepository roleRepository;

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
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDTO.getUsername(),
                        userDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt));


//        User createdUser = userService.login(userDTO.getUsername(),userDTO.getPassword());
//        if(createdUser == null){
//            if(userService.findUserByUsername(userDTO.getUsername()) != null){
//                ApiResult<?> apiResult = new ApiResult<>("Your password account is incorrect");
//                return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
//            }
//            ApiResult<?> apiResult = new ApiResult<>("Your account doesn't sign up before");
//            return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
//        }
//        UserResponseDTO userResponseDTO = modelMapper.map(createdUser, UserResponseDTO.class);
//        ApiResult<?> apiResult = new ApiResult<>(userResponseDTO,"Your account has been signed in successfully");
//        return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
    }

    @PostMapping("/signup")
    public ResponseEntity registerUser(@RequestBody UserCreateDTO userDTO) {
        if(userRepository.findByUsername(userDTO.getUsername()) != null) {
            return new ResponseEntity("Fail -> Username is already taken!",
                    HttpStatus.BAD_REQUEST);
        }
//
//        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
//            return new ResponseEntity("Fail -> Email is already in use!",
//                    HttpStatus.BAD_REQUEST);
//        }

        // Creating user's account
        User reqUser = modelMapper.map(userDTO, User.class);
        reqUser.setPassword(encoder.encode(userDTO.getPassword()));
        reqUser.setActive(true);
        reqUser.setCreatedAt(StaticFuntion.getDate());
        reqUser.setPhoneNumber(userDTO.getPhoneNumber());
        reqUser.setRole(roleService.findRoleByRoleName("ROLE_USER"));
        User createdUser = userService.saveUser(reqUser);

        UserResponseDTO userResponseDTO = modelMapper.map(createdUser, UserResponseDTO.class);
        ApiResult<?> apiResult = new ApiResult<>(userResponseDTO,"Your account has been registed successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
    }
}
