package edu.sdccd.cisc191.common.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class User implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ; // Unique numerical identifier for each user, regardless of status as admin/broker/customer

    @Column
    private String name;                    //the users selected account name

    @Column
    private String passwordHash;

    @Column
    private Role role;

    public static enum Role {
        Regular,
        Admin,
    }

    public User(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{"
                    + "id=" + this.id + ","
                    + "name='" + this.name + "'"
                + "}";
    }
}
