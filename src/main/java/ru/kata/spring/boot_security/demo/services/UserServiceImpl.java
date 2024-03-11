package ru.kata.spring.boot_security.demo.services;


import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByFirstName(username);
    }

    @Override
    public Set<Role> getUserRoles(String username) {
        Optional<User> userOptional = userRepository.findByFirstName(username);
        return userOptional.map(User::getRoles).orElse(Collections.emptySet());
    }
}
