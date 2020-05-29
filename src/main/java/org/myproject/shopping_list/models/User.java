package org.myproject.shopping_list.models;

import org.myproject.shopping_list.validation.ValidEmail;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity
public class User implements UserDetails{
    public static Object passwordEncoder;
    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private String username;
    @NotNull
    private String password;
    @ValidEmail
    @NotEmpty
    @NotNull
    private String email;

    @OneToMany
    @JoinColumn(name="user_id")
    private List<Item> userItems= new ArrayList<>();

    @OneToMany
    @JoinColumn(name="user_id")
    private List<GroceryList> userGroceryLists= new ArrayList<>();

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    private boolean enabled;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return Arrays.asList(new SimpleGrantedAuthority("USER"));
    }
    public User(){};

    public User (String username, String password, String email){
        this.username=username;
        this.password=encoder.encode(password);
        this.email=email;
        this.enabled=false;
    }
    public static final BCryptPasswordEncoder encoder= new BCryptPasswordEncoder();

    public static User getCurrentUser(){
        Object loggedInUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = (User) loggedInUser;
        return currentUser;
    }

    public boolean isMatchingPassword(String verifyPassword){
        return encoder.matches(verifyPassword, password);
    }

    public int getId() {
        return id;
    }

    public List<Item> getItems() {
        return userItems;
    }

    public void setItems(List<Item> items) {
        this.userItems = items;
    }

    public List<GroceryList> getGroceryLists() {
        return userGroceryLists;
    }

    public void setGroceryLists(List<GroceryList> groceryLists) {
        this.userGroceryLists = groceryLists;
    }

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
        this.password = encoder.encode(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
