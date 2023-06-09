package com.core.repository;

import com.core.entity.Role;
import com.core.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleType(RoleType name);

    @Query(nativeQuery = true,value = "select * from roles r where r.role_type = :roleType")
    Role getByRoleType(@Param("roleType") String roleType);

}
