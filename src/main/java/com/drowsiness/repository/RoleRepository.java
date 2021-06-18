package com.drowsiness.repository;

import com.drowsiness.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    @Query("SELECT r FROM Role r WHERE r.roleName = :roleName")
    Role findRoleByRoleName(@Param("roleName") String roleName);

    @Query("SELECT r FROM Role r WHERE r.roleId = (SELECT u.role.roleId FROM User u WHERE u.username = :username)")
    Role findRoleByUserName(@Param("username") String username);
}
