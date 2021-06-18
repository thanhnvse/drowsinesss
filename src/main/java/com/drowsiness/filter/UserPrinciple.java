package com.drowsiness.filter;

import com.drowsiness.dto.role.RoleResponseDTO;
import com.drowsiness.dto.user.UserAuthenDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
public class UserPrinciple implements UserDetails {
    private static final long serialVersionUID = 1L;

    private UUID userId;

    private String fullName;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private Collection authorities;

    public UserPrinciple(UUID userId, String fullName,
                         String username, String email, String password,
                         Collection authorities) {
        this.userId = userId;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public UserPrinciple(String fullName,
                         String username, String email, String password,
                         Collection authorities) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserPrinciple build(UserAuthenDTO user) {
        RoleResponseDTO authority = user.getRoleResponseDTO();
        List authorities = new ArrayList();
        authorities.add(new SimpleGrantedAuthority(authority.getRoleName()));
        return new UserPrinciple(
                user.getUserId(),
                user.getFullName(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserPrinciple user = (UserPrinciple) o;
        return Objects.equals(userId, user.userId);
    }
}
