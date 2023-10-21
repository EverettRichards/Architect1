package edu.sdccd.cisc191.server.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.sdccd.cisc191.common.entities.User;
import edu.sdccd.cisc191.server.errors.DatabaseError;
import edu.sdccd.cisc191.server.errors.UserNotFound;
import edu.sdccd.cisc191.server.services.UserService;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/user")
public class AccountController {

    @Autowired(required = true)
    private UserService userService;


    @GetMapping("/sanitycheck")
    public String ineedsanity() {
        return "yes, heres ur sanity";
    }
    
    @PostMapping("/add")
    public void add(User user) throws DatabaseError {
        // System.out.println(user.getName());
        userService.createUser(user);
    }

    @PostMapping("/update/{id}")
    public void update(@PathVariable Long id, User newUserData) throws UserNotFound {
        // System.out.println(user.getName());
        Optional<User> oldUser = userService.getUserById(id);
        if(oldUser.isEmpty()) {
            throw new UserNotFound();
        }
        userService.updateUser(oldUser.get(), newUserData);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws DatabaseError {
        Optional<User> user = userService.getUserById(id);
        if(user.isEmpty()) {
            throw new UserNotFound();
        }

        userService.deleteUser(user.get());
    }
}
