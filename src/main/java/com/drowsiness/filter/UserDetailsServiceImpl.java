package com.drowsiness.filter;

import com.drowsiness.dto.role.RoleResponseDTO;
import com.drowsiness.dto.user.UserAuthenDTO;
import com.drowsiness.model.Role;
import com.drowsiness.model.User;
import com.drowsiness.repository.RoleRepository;
import com.drowsiness.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    UserRepository userRepository;

    RoleRepository roleRepository;

    ModelMapper modelMapper;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuthenDTO user = new UserAuthenDTO();
        try{
            User userMapper = userRepository.findByUsername(username);
            user = modelMapper.map(userMapper, UserAuthenDTO.class);
            Role role =  roleRepository.findRoleByUserName(username);
            RoleResponseDTO roleResponseDTO = modelMapper.map(role, RoleResponseDTO.class);
            user.setRoleResponseDTO(roleResponseDTO);
        }catch (UsernameNotFoundException ex){
            new UsernameNotFoundException("User Not Found with -> username or email : " + username);
        }

//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() ->
//                        new UsernameNotFoundException("User Not Found with -> username or email : " + username)
//                );
        return UserPrinciple.build(user);
    }
}
