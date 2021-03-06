package org.myproject.shopping_list.controllers;

import org.myproject.shopping_list.error.ItemNotFoundException;
import org.myproject.shopping_list.models.ConfirmationToken;
import org.myproject.shopping_list.models.GroceryList;
import org.myproject.shopping_list.models.Item;
import org.myproject.shopping_list.models.User;
import org.myproject.shopping_list.repository.ConfirmationTokenRepository;
import org.myproject.shopping_list.repository.UserRepository;
import org.myproject.shopping_list.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value="user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    @GetMapping(value = "")
    public String currentUser(Model model){

        User currentUser = User.getCurrentUser();
        int id = currentUser.getId();
        return "redirect:/user/" + id;
    }
    @GetMapping(value="/{id}")
    public String userDetails(@PathVariable int id, Model model) throws ItemNotFoundException {
        User user=itemService.getUserById(id);
        model.addAttribute("title", "EZList");
        model.addAttribute("user", user);
        model.addAttribute("items", user.getItems());
        model.addAttribute("groceryList", user.getGroceryLists());
        return "user/index";
    }

    @GetMapping(value="/{id}/account")
    public String viewAccount(@PathVariable int id, Model model) throws ItemNotFoundException {
        User user= itemService.getUserById(id);
        model.addAttribute("title", "EZList");
        model.addAttribute("user", user);

        return "user/account";
    }

    @GetMapping(value="/{id}/edit")
    public String editUser(@PathVariable int id, Model model){
        Optional<User> optUser= userRepository.findById(id);
        User user= optUser.get();

        model.addAttribute("title", "EZList");
        model.addAttribute("user", user);

        return "user/edit";
    }

    @PostMapping(value="edit")
    public String processEditUser(@ModelAttribute @Valid User user, Errors errors, String username,
                                   String email, String password, String verifyPassword, int userId,
                                   Model model) throws ItemNotFoundException {
        User existingUser=itemService.getUserById(userId);
        if (errors.hasErrors()){
            model.addAttribute("title", "Edit User");
            model.addAttribute("existingUserId", existingUser.getId());
            model.addAttribute("user", user);
            return "user/edit";
        }
        if (!password.equals(verifyPassword)) {
            if (!password.equals("******")) {
                model.addAttribute("title", "Edit User");
                model.addAttribute("verifyError", "Passwords do not match");
                model.addAttribute("user", existingUser);
                return "user/edit";
            }
        }

        existingUser.setUsername(username);
        existingUser.setEmail(email);
        if (!password.equals("******")) {
            existingUser.setPassword(password);
        }
        userRepository.save(existingUser);

        return "redirect:/user/" + userId;
    }
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    public String deleteUser(@PathVariable int id, Model model) throws ItemNotFoundException {

        User currentUser = User.getCurrentUser();
        int currentUserId = currentUser.getId();

        if(currentUserId == id) {
            User user=itemService.getUserById(id);
            List<Item> items= itemService.getAllItemsByUser(user);
            List <GroceryList> groceryLists= itemService.getAllGroceryListsByUser(user);
            for (Item item : items) {
                if (item.getUser().getId() == id) {
                    itemService.deleteItemById(item.getId());
                }
            }
            for (GroceryList groceryList: groceryLists){
                if (groceryList.getGroceryUser().getId()==id){
                    itemService.deleteGroceryListById(groceryList.getId());
                }
            }
            List<ConfirmationToken> token=itemService.getConfirmationTokenByUserId(id);
            confirmationTokenRepository.delete(token.get(0));
            userRepository.delete(user);
            SecurityContextHolder.clearContext();
        }

        return "redirect:/";

    }
}
