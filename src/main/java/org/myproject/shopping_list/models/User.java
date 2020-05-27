package org.myproject.shopping_list.models;

import org.myproject.shopping_list.validation.ValidEmail;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class User extends AbstractEntity{
    @NotNull
    private String username;
    @NotNull
    private String pwHash;
    @ValidEmail
    @NotEmpty
    @NotNull
    private String email;

    private Boolean isEnabled;

    public User(){};

    public User (String username, String password, String email){
        this.username=username;
        this.pwHash=encoder.encode(password);
        this.email=email;
        this.isEnabled=false;
    }
    private static final BCryptPasswordEncoder encoder= new BCryptPasswordEncoder();

    public boolean isMatchingPassword(String password){
        return encoder.matches(password, pwHash);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwHash() {
        return pwHash;
    }

    public void setPwHash(String pwHash) {
        this.pwHash = pwHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }
}
