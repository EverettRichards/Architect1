package edu.sdccd.cisc191.server.services;

import org.springframework.stereotype.Service;

import edu.sdccd.cisc191.common.entities.User;
import edu.sdccd.cisc191.server.errors.DatabaseError;

import java.util.Optional;

@Service
public interface UserService {
    void createUser(User user) throws DatabaseError;
    void deleteUser(User user) throws DatabaseError;
    void updateUser(User user, User modified) throws DatabaseError;
    Optional<User> getUser(Long id);
    Optional<User> getUser(String username);
    boolean userExists(Long id);
    boolean userExists(String username);
}
