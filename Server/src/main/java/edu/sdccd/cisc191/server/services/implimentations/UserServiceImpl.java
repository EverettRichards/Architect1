package edu.sdccd.cisc191.server.services.implimentations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sdccd.cisc191.server.services.UserService;
import edu.sdccd.cisc191.common.entities.User;
import edu.sdccd.cisc191.server.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    // @Transactional
    @Override
    public void createUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void updateUser(User user, User modified) {
        userRepository.updateUser(user.getName(), user.getPasswordHash(), user.getId());
    }

    @Override
    public boolean userExists(Long id) {
        return !this.getUserById(id).isEmpty();
    }
}