package edu.sdccd.cisc191.common.entities;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testGettersAndSetters() {
        // Create a User object for testing
        User user = new User();

        // Test setter and getter for id
        user.setId(1L);
        assertEquals(1L, user.getId());

        // Test setter and getter for email
        user.setEmail("test@example.com");
        assertEquals("test@example.com", user.getEmail());

        // Test setter and getter for name
        user.setName("testUser");
        assertEquals("testUser", user.getName());

        // Test setter and getter for nickname
        user.setNickname("testNickname");
        assertEquals("testNickname", user.getNickname());

        // Test setter and getter for passwordHash
        user.setPasswordHash("testPasswordHash");
        assertEquals("testPasswordHash", user.getPasswordHash());

        // Test setter and getter for role
        user.setRole(User.Role.Admin);
        assertEquals(User.Role.Admin, user.getRole());

        // Test setter and getter for followedTickers
        user.setFollowedTickers(List.of("AAPL", "GOOGL"));
        assertEquals(List.of("AAPL", "GOOGL"), user.getFollowedTickers());
    }
}