package org.myproject.shopping_list.controllers;

import org.myproject.shopping_list.error.ItemNotFoundException;
import org.myproject.shopping_list.models.GroceryList;
import org.myproject.shopping_list.models.LastBought;
import org.myproject.shopping_list.models.User;
import org.myproject.shopping_list.repository.GroceryListRepository;
import org.myproject.shopping_list.repository.ItemRepository;
import org.myproject.shopping_list.repository.UserRepository;
import org.myproject.shopping_list.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("groceries")
public class GroceryListController {
    @Autowired
    private GroceryListRepository groceryListRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "")
    public String currentUser(Model model){
        User currentUser = User.getCurrentUser();
        int id = currentUser.getId();
        return "redirect:/groceries/" + id;
    }

    @GetMapping(value="{userId}")
    public String displayAllGroceryLists(Model model,@PathVariable int userId) {
        User user=itemService.getUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("title", "My Lists");
        List<GroceryList> groceryList= itemService.getAllGroceryListsByUser(user);
        model.addAttribute("lists", groceryList);

        return "groceries/index";
    }

    @GetMapping("{userId}/new")
    public String displayNewGroceryList(Model model, @PathVariable int userId){
        User user=itemService.getUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("title", "New Grocery List");
        model.addAttribute(new GroceryList());
        model.addAttribute("items", itemService.getAllItemsByUser(user));
        return "groceries/new";
    }
    @RequestMapping(path="new", method = RequestMethod.POST)
    public String processNewGroceryListForm(@ModelAttribute @Valid GroceryList newGroceryList, Errors errors,
                                            Model model, @RequestParam(value="items", required=false) List<Integer> items,
                                            @RequestParam(value="userId") int userId){
        User user=itemService.getUserById(userId);
        model.addAttribute("user", user);
        if (errors.hasErrors()){
            model.addAttribute("title", "New Grocery List");
            model.addAttribute("items", itemService.getAllItemsByUser(user));
            return "groceries/new";
        }
        newGroceryList.setGroceryUser(user);
//        user.addGroceryList(newGroceryList);
        itemService.createGroceryList(items, newGroceryList);
        return "redirect:";

    }

    @GetMapping("{userId}/view/{groceryListId}")
    public String displayGroceryList(Model model, @PathVariable int groceryListId, @PathVariable int userId)
                                     throws ItemNotFoundException{
        User user= itemService.getUserById(userId);
        GroceryList list=itemService.getGroceryById(groceryListId);
        model.addAttribute("user", user);
        model.addAttribute("list", list);
        return "groceries/view";
    }

    @GetMapping("{userId}/shopping/{groceryListId}")
    public String displayMyShoppingList(Model model, @PathVariable int groceryListId,
                                        LastBought day, @PathVariable int userId)
                                        throws ItemNotFoundException{
        User user= itemService.getUserById(userId);
        GroceryList list=itemService.getGroceryById(groceryListId);
        LocalDateTime date=day.getLastBought();
        model.addAttribute("date", LastBought.convertLastBought(date));
        model.addAttribute("user", user);
        model.addAttribute("list", list);
        return "groceries/shopping";
    }

    @RequestMapping(path="shopping/{groceryListId}", method=RequestMethod.POST)
    public String updateItemLastBought(Model model, @RequestParam(value="bought") List<Integer> itemIds,
                                       @RequestParam(value="userId")int userId, @PathVariable int groceryListId)
            throws ItemNotFoundException {
        User user=itemService.getUserById(userId);
        model.addAttribute("user", user);
        if (itemIds != null) {
            for (int id : itemIds) {
               itemService.setItemTime(LocalDateTime.now(), id);
            }
        }
        return "redirect:/groceries/"+userId+"/view/"+groceryListId;
    }

    @RequestMapping(value={"/{userId}/delete/{groceryListId}", "/{userId}/delete/{groceryListId}/{itemId}" }, method=RequestMethod.GET)
    public String deleteFromGroceryListById(Model model, @PathVariable(required=false) Integer itemId,
                                            @PathVariable Integer groceryListId, @PathVariable int userId )
    throws ItemNotFoundException {
        if (itemId != null){
            itemService.deleteItemFromGroceryById(groceryListId, itemId);
            return "redirect:/groceries/"+userId+"/view/"+groceryListId;
        } else {
            itemService.deleteGroceryListById(groceryListId);
            return "redirect:/groceries";
        }
    }

}
