package com.drowsiness.repository;

import com.drowsiness.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM User u WHERE u.username = :username And u.password = :password")
    User findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(@Param("email") String email);

    @Query("SELECT u.userId, u.fullName, u.username, u.password, u.email FROM User u WHERE u.username = :username")
    User findSpecificByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.role.roleName = 'ROLE_USER'")
    List<User> getAllUserWithoutAdminRole();
}
