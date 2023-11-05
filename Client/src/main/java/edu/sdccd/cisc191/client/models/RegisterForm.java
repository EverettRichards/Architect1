package edu.sdccd.cisc191.client.models;

import lombok.Data;
import lombok.NonNull;

@Data
public class RegisterForm {
    @NonNull
    private String username;

    @NonNull
    private String nickname;
    
    @NonNull
    private String password;

    @NonNull
    private String email;
}
