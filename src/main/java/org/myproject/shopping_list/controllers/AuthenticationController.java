package org.myproject.shopping_list.controllers;

import org.myproject.shopping_list.models.ConfirmationToken;
import org.myproject.shopping_list.models.User;
import org.myproject.shopping_list.repository.ConfirmationTokenRepository;
import org.myproject.shopping_list.repository.UserRepository;
import org.myproject.shopping_list.dto.LoginFormDTO;
import org.myproject.shopping_list.dto.RegisterFormDTO;
import org.myproject.shopping_list.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("")
public class AuthenticationController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    private EmailSenderService emailSenderService;

    private static final String userSessionKey= "user";

    public User getUserFromSession(HttpSession session){
        Integer userId= (Integer) session.getAttribute(userSessionKey);
        if (userId==null){
            return null;
        }
        Optional<User> user=userRepository.findById(userId);
        if (user.isEmpty()){
            return null;
        }
        return user.get();
    }

    private static void setUserInSession(HttpSession session, User user){
        session.setAttribute(userSessionKey, user.getId());
    }

    @GetMapping(value="")
    public String index(Model model){
        Object loggedInUser= SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (loggedInUser.equals("anonymousUser")){
            model.addAttribute("title", "EZList");
            return "/index";
        }
        return "/register";
    }

    @GetMapping("/register")
    public String displayRegistrationForm(Model model){
        model.addAttribute(new RegisterFormDTO());
        model.addAttribute("title", "Register");
        return "register";
    }

    @PostMapping("/register")
    public String processRegistrationForm(@ModelAttribute @Valid RegisterFormDTO registerFormDTO,
                                          Errors errors, HttpServletRequest request,
                                          Model model){
        if (errors.hasErrors()){
            model.addAttribute("title", "Register");
            return "/register";
        }

        User existingUser= userRepository.findByUsername(registerFormDTO.getUsername());

        if(existingUser !=null){
            errors.rejectValue("username", "username.alreadyexists", "A user with that username already exists");
            model.addAttribute("title", "Register");
            return "/register";
        }

        User existingEmail= userRepository.findByEmail(registerFormDTO.getEmail());

        if(existingEmail !=null){
            errors.rejectValue("email", "email.alreadyexists", "A user with that email already exists");
            model.addAttribute("title", "Register");
            return "/register";
        }

        String password= registerFormDTO.getPassword();
        String verifyPassword= registerFormDTO.getVerifyPassword();
        if (!password.equals(verifyPassword)){
            errors.rejectValue("password", "passwords.mismatch", "Passwords do not match");
            model.addAttribute("title","Registration");
            return "/register";
        }

        User newUser= new User(registerFormDTO.getUsername(), registerFormDTO.getPassword(), registerFormDTO.getEmail());
        userRepository.save(newUser);

        //MAIL SENDER
        ConfirmationToken confirmationToken= new ConfirmationToken(newUser);
        confirmationTokenRepository.save(confirmationToken);
        SimpleMailMessage mailMessage= new SimpleMailMessage();
        mailMessage.setTo(newUser.getEmail());
        mailMessage.setSubject("Complete Registration");
        mailMessage.setFrom("aokuonghae@gmail.com");
        mailMessage.setText("To confirm your account, please click here : "
                +"http://localhost:8080/confirm-account?token="+ confirmationToken.getConfirmationToken());
        emailSenderService.sendEmail(mailMessage);

        model.addAttribute("email", newUser.getEmail());

        setUserInSession(request.getSession(), newUser);
        return "/successful_registration";
    }


    @GetMapping("/confirm-account")
    public String confirmUserAccount(Model model, @RequestParam("token") String confirmationToken){
        ConfirmationToken token= confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if (token !=null) {
            User user= userRepository.findByEmail(token.getUser().getEmail());
            user.setEnabled(true);
            userRepository.save(user);
            model.addAttribute("message", "true");
        } else {
            model.addAttribute("message","false");
        }
        return "/confirmation";
    }

    @GetMapping("/login")
    public String displayLoginForm(Model model){
        model.addAttribute(new LoginFormDTO());
        model.addAttribute("title", "Login");
        return "login";
    }

    @PostMapping("/login")
    public String processLoginForm(@ModelAttribute @Valid LoginFormDTO loginFormDTO,
                                   Errors errors, HttpServletRequest request,
                                   Model model){
        if (errors.hasErrors()){
            model.addAttribute("title", "Log in");
            return "login";
        }
        User theUser= userRepository.findByUsername(loginFormDTO.getUsername());

        if(theUser ==null){
            errors.rejectValue("username", "user.invalid", "The given username does not exist");
            model.addAttribute("title", "Log In");
            return "login";
        }
        String password=loginFormDTO.getPassword();

        if(!theUser.isMatchingPassword(password)){
            errors.rejectValue("password", "password.invalid", "Invalid password");
            model.addAttribute("title", "Log In");
            return "login";
        }
        setUserInSession(request.getSession(), theUser);
        return "redirect:/user";
    }

    @GetMapping("/logout")
    public String logout (HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/login";
    }

}