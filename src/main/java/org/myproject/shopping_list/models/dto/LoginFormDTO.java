package org.myproject.shopping_list.models.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginFormDTO {
    @NotNull
    @NotBlank
    @Size(min=3, max=20, message="Invalid username. Must be between 3 and 20.")
    private String username;

    @NotNull
    @NotBlank
    @Size(min=3, max=20, message="Invalid username. Must be between 3 and 20.")
    private String password;

    @Email
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

