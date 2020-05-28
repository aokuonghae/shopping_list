package org.myproject.shopping_list.security;

import org.myproject.shopping_list.models.User;
import org.myproject.shopping_list.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/register", "/register/*",  "/login", "/css/*", "/js/*", "/index", "/successful_registration", "/confirm-account", "/img/*").permitAll()
                .antMatchers( "/item", "/item/*", "/groceries","/groceries/*", "/user", "/user/*", "/files").hasAuthority("USER")

                .and()

                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/user")

                .and()

                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
        ;
    }

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(new UserDetailsService() {
                    @Override
                    public UserDetails loadUserByUsername(String username)
                            throws UsernameNotFoundException {
                        return userRepository.findByUsername(username);
                    }
                })
                .passwordEncoder(User.encoder)
        ;
    }

}
