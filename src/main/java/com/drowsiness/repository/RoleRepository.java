package com.drowsiness.repository;

import com.drowsiness.model.Role;
import com.drowsiness.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    @Query("SELECT r FROM Role r WHERE r.roleName = :roleName")
    Role findRoleByRoleName(@Param("roleName") String roleName);
}
