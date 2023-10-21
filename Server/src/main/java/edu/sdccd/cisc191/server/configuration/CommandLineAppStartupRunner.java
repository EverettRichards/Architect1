package edu.sdccd.cisc191.server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import edu.sdccd.cisc191.common.cryptography.Hasher;
import edu.sdccd.cisc191.common.entities.User;
import edu.sdccd.cisc191.server.services.UserService;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    @Autowired
    private UserService userService;

    @Override
    public void run(String...args) throws Exception {
        System.out.println("lmoolololololol");
        if(!userService.userExists("admin")) {
            String passwordHash = Hasher.hashNewPassword("admin");
            System.out.println(passwordHash);
            User admin = new User("admin", "admin", passwordHash, User.Role.Admin);
            userService.createUser(admin);
        }
    }
}
