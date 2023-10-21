package edu.sdccd.cisc191.server.services.implimentations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sdccd.cisc191.server.services.UserService;
import edu.sdccd.cisc191.common.entities.User;
import edu.sdccd.cisc191.server.errors.DatabaseError;
import edu.sdccd.cisc191.server.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    // @Transactional
    @Override
    public void createUser(User user) throws DatabaseError {
        try {
            userRepository.save(user);
        } catch(IllegalArgumentException e) {
            throw new DatabaseError(e.toString());
        }
    }

    @Override
    public void deleteUser(User user) throws DatabaseError {
        try {
            userRepository.delete(user);
        } catch(IllegalArgumentException e) {
            throw new DatabaseError(e.toString());
        }
    }

    @Override
    public void updateUser(User user, User modified) throws DatabaseError {
        try {
            userRepository.updateUser(user.getName(), user.getNickname(), user.getPasswordHash(), user.getId());
        } catch(Exception e) {
            throw new DatabaseError(e.toString());
        }
    }


    @Override
    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUser(String username) {
        return userRepository.findByName(username);
    }

    @Override
    public boolean userExists(Long id) {
        return !this.getUser(id).isEmpty();
    }

    @Override
    public boolean userExists(String username) {
        return !this.getUser(username).isEmpty();
    }
}