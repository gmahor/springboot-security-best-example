package com.core;

import com.core.entity.Role;
import com.core.enums.RoleType;
import com.core.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;

    @Autowired
    public Bootstrap(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void createRole() {
        List<Role> roles = new ArrayList<>();
        Role role = new Role(RoleType.ROLE_ADMIN);
        Role role1 = new Role(RoleType.ROLE_MODERATOR);
        Role role2 = new Role(RoleType.ROLE_USER);
        roles.add(role);
        roles.add(role1);
        roles.add(role2);
        roles.forEach(roleObj -> {
            Optional<Role> optionalRole = roleRepository.findByRoleType(roleObj.getRoleType());
            if (!optionalRole.isPresent()) {
                Role savedRole = roleRepository.save(roleObj);
                log.info("role created successfully : roleName : {}", savedRole.getRoleType().name());
            }
        });

    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            createRole();
        } catch (Exception e) {
            log.error("Exception In On Application Event Service - ", e);
        }
    }


}
