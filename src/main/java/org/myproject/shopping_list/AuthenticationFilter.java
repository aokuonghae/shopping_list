package org.myproject.shopping_list;

import org.myproject.shopping_list.controllers.AuthenticationController;
import org.myproject.shopping_list.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


public class AuthenticationFilter extends HandlerInterceptorAdapter {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationController authenticationController;
}

