package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Lazy;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationServiceImpl(@Lazy UserRepository userRepository, RoleService roleService,
                                   PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registrationAdmin(User user) {
        String[] roles = new String[]{"ROLE_ADMIN"};
        setUserRoles(user, roles);
        userRepository.save(user);
    }

    @Override
    public void registrationUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String[] roles = new String[]{"ROLE_USER"};
        setUserRoles(user, roles);

        userRepository.save(user);
    }

    @Override
    public void setUserRoles(User user, String[] roles) {
        Set<Role> rolesSet = new HashSet<>();
        for (String roleName : roles) {
            Role role = roleService.findByName(roleName);
            if (role == null) {
                role = new Role(roleName);
                roleService.saveRole(role);
            }
            rolesSet.add(role);
        }
        user.setRoles(rolesSet);
    }
}
