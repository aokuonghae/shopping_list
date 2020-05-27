package org.myproject.shopping_list.models;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
public class User extends AbstractEntity{
    @NotNull
    private String username;
    @NotNull
    private String pwHash;
    @Email
    private String email;

    public User(){};

    public User (String username, String password, String email){
        this.username=username;
        this.pwHash=encoder.encode(password);
        this.email=email;
    }

    private static final BCryptPasswordEncoder encoder= new BCryptPasswordEncoder();

    public boolean isMatchingPassword(String password){
        return encoder.matches(password, pwHash);
    }
}
