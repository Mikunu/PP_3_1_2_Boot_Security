package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.AdminService;
import ru.kata.spring.boot_security.demo.services.RegistrationService;
import ru.kata.spring.boot_security.demo.services.RoleService;

import javax.annotation.PostConstruct;

@Component
public class DataInit {

    private final AdminService adminService;
    private final RoleService roleService;
    private final RegistrationService registrationService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInit(AdminService adminService, RoleService roleService,
                    RegistrationService registrationService, PasswordEncoder passwordEncoder) {
        this.adminService = adminService;
        this.roleService = roleService;
        this.registrationService = registrationService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initData() {
        if (adminService.getAllUsers().isEmpty()) {
            Role adminRole = new Role("ROLE_ADMIN");
            Role userRole = new Role("ROLE_USER");
            roleService.saveRole(adminRole);
            roleService.saveRole(userRole);
            User adminUser = new User();
            adminUser.setFirstName("admin");
            adminUser.setLastName("admin");
            adminUser.setPassword(passwordEncoder.encode("admin"));
            registrationService.registrationAdmin(adminUser);
            System.out.println("Users created:");
            System.out.println("Admin: username=admin, password=admin");
        }
    }
}
