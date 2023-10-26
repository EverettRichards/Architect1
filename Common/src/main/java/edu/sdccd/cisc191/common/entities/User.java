package edu.sdccd.cisc191.common.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ; // Unique numerical identifier for each user, regardless of status as admin/broker/customer

    @NotNull
    @Column
    private String email;

    @NotNull
    @Column(unique = true)
    private String name;                    //the users selected account name

    @NotNull
    @Column
    private String nickname; // can be empty

    @Column(length = 512)
    private String passwordHash;

    @NotNull
    @Column
    private Role role;

    public static enum Role {
        Regular,
        Admin,
    }

    public User(String email, String name, String nickname, String passwordHash, Role role) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.passwordHash = passwordHash;
        this.role = role;
    }
}
