package edu.sdccd.cisc191.common.entities;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;

public class Customer extends User {
    @NotNull
    @Column
    private StockArrayList followedStocks;

    public Customer(String email, String name, String nickname, String passwordHash, Role role, StockArrayList followedStocks) {
        super(email,name,nickname,passwordHash,role);
        this.followedStocks = followedStocks;
    }
}
