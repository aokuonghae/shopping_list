package org.myproject.shopping_list.dto;

import org.myproject.shopping_list.validation.ValidEmail;

import javax.validation.constraints.*;

public class LoginFormDTO {

    @NotNull
    @NotBlank (message="You must enter a username")
    @Size(min=3, max=20, message="Invalid username. Must be between 3 and 20.")
    private String username;

    @NotNull
    @NotBlank (message="You must enter a password")
    @Size(min=3, max=20, message="Invalid password. Must be between 3 and 20.")
    private String password;

    @NotNull (message="You must enter a valid email.")
    @NotEmpty
    @ValidEmail(message="Invalid Email. Enter a valid email.")
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

