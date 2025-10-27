package com.plague.nexus.core;

import com.plague.nexus.user.role.Role;
import com.plague.nexus.user.role.RoleRepository;
import com.plague.nexus.user.role.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // We check if the roles already exist. If not, we create them.
        // This makes the app "idempotent" - you can run it many times
        // and it won't create duplicate roles.

        if (roleRepository.findByName(RoleType.ROLE_USER).isEmpty()) {
            roleRepository.save(new Role(RoleType.ROLE_USER));
        }
        if (roleRepository.findByName(RoleType.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(new Role(RoleType.ROLE_ADMIN));
        }
        if (roleRepository.findByName(RoleType.ROLE_WAREHOUSE).isEmpty()) {
            roleRepository.save(new Role(RoleType.ROLE_WAREHOUSE));
        }
        if (roleRepository.findByName(RoleType.ROLE_ACCOUNTING).isEmpty()) {
            roleRepository.save(new Role(RoleType.ROLE_ACCOUNTING));
        }
    }
}