package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.Optional;
import java.util.Set;

public interface UserService {
    public Optional<User> loadUserByUsername(String username) throws UsernameNotFoundException;

    public Set<Role> getUserRoles(String username);
}
