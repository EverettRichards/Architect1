package edu.sdccd.cisc191.server.api;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.sdccd.cisc191.common.entities.User;
import edu.sdccd.cisc191.server.errors.DatabaseError;
import edu.sdccd.cisc191.server.errors.UserExists;
import edu.sdccd.cisc191.server.errors.UserNotFound;
import edu.sdccd.cisc191.server.services.implimentations.UserServiceImpl;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/user")
public class AccountController {

    @Autowired(required = true)
    private UserServiceImpl userService;

    @GetMapping("/sanitycheck")
    public String ineedsanity() {
        return "yes, heres ur sanity";
    }
    
    @PostMapping("/add")
    public User add(@RequestBody User user) throws UserExists {
        // System.out.println(user.getName());
        userService.createUser(user);

        return userService.getUser(user.getName()).get();
    }

    @PostMapping("/update/{id}")
    public void update(@PathVariable Long id, @RequestBody User newUserData) throws UserNotFound {
        Optional<User> oldUser = userService.getUser(id);
        if(oldUser.isEmpty()) {
            throw new UserNotFound();
        }

        oldUser.get().setName(newUserData.getName());
        oldUser.get().setNickname(newUserData.getNickname());
        oldUser.get().setEmail(newUserData.getEmail());
        oldUser.get().setFollowedTickers(newUserData.getFollowedTickers());

        userService.updateUser(oldUser.get());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws DatabaseError {
        System.out.println(id);
        Optional<User> user = userService.getUser(id);
        if(user.isEmpty()) {
            throw new UserNotFound();
        }

        userService.deleteUser(user.get());
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) throws DatabaseError {
        Optional<User> user = userService.getUser(id);
        if(user.isEmpty()) {
            throw new UserNotFound();
        }
        return user.get();
    }

    @GetMapping("/name/{name}")
    public User getByName(@PathVariable String name) throws DatabaseError {
        Optional<User> user = userService.getUser(name);
        if(user.isEmpty()) {
            throw new UserNotFound();
        }
        return user.get();
    }

    @GetMapping("/all")
    public List<User> getAll() {
        return userService.getAllUsers();
    }
}
