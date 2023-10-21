package edu.sdccd.cisc191.server.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.sdccd.cisc191.common.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query(value = "update users u set u.name = ?1, u.password = ?2 where u.id = ?3", nativeQuery = true)
    void updateUser(String name, String password, Long id);
}