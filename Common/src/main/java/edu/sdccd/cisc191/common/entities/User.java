package edu.sdccd.cisc191.common.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

    @ElementCollection
    @CollectionTable(name = "followedTickers", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "tickers")
    private List<String> followedTickers;

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
        this.followedTickers = new ArrayList<>();
    }
    public User(String email, String name, String nickname, String passwordHash, Role role, ArrayList<String> followedTickers) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.passwordHash = passwordHash;
        this.role = role;
        this.followedTickers = followedTickers;
    }

    public Role getRole() {
        return this.role;
    }

    public String getRoleAsString() {
        if (this.role == Role.Admin) {
            return "ROLE_ADMIN";
        } else {
            return "ROLE_USER";
        }
    }
}
