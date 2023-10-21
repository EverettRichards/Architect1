package edu.sdccd.cisc191.server.services;

import org.springframework.stereotype.Service;

import edu.sdccd.cisc191.common.entities.User;

import java.util.Optional;

@Service
public interface UserService {
    void createUser(User user);
    void deleteUser(User user);
    void updateUser(User user, User modified);
    Optional<User> getUserById(Long id);
    boolean userExists(Long id);
}
