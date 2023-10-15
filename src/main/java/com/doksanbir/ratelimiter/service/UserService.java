package com.doksanbir.ratelimiter.service;

import com.doksanbir.ratelimiter.model.Role;
import com.doksanbir.ratelimiter.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    User createUser(User user);

    Optional<User> getUserById(Long id);

    Optional<User> getUserByUsername(String username);

    List<User> getAllUsers();

    User updateUser(User user);

    void deleteUser(Long id);

    Set<Role> getRolesByUserId(Long id);
}
