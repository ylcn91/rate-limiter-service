package com.doksanbir.ratelimiter.service;

import com.doksanbir.ratelimiter.model.Role;
import com.doksanbir.ratelimiter.model.User;
import com.doksanbir.ratelimiter.repository.RoleRepository;
import com.doksanbir.ratelimiter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public User createUser(User user) {
        log.info("Creating new user with username: {}", user.getUsername());
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        log.info("Fetching user by ID: {}", id);
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        log.info("Fetching user by username: {}", username);
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        log.info("Updating user with ID: {}", user.getId());
        if (userRepository.existsById(user.getId())) {
            return userRepository.save(user);
        } else {
            log.error("User with ID {} not found", user.getId());
            throw new IllegalArgumentException("User does not exist");
        }
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            log.error("User with ID {} not found", id);
            throw new IllegalArgumentException("User does not exist");
        }
    }

    @Override
    public Set<Role> getRolesByUserId(Long id) {
        log.info("Fetching roles for user ID: {}", id);
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get().getRoles();
        } else {
            log.error("User with ID {} not found", id);
            return new HashSet<>();
        }
    }
}

