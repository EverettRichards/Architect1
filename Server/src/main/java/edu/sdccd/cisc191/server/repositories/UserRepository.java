package edu.sdccd.cisc191.server.repositories;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.sdccd.cisc191.common.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query(value = "update users u set u.email = ?1, u.name = ?2, u.nickname = ?3, u.password = ?4 where u.id = ?5", nativeQuery = true)
    void updateUser(String email, String name, String nickname, String password, Long id);

    @Query(value = "SELECT * FROM users WHERE name = ?1", nativeQuery = true)
    Optional<User> findByName(String username);
}