package org.myproject.shopping_list.controllers;

import org.myproject.shopping_list.error.ItemNotFoundException;
import org.myproject.shopping_list.models.GroceryList;
import org.myproject.shopping_list.models.Item;
import org.myproject.shopping_list.models.ItemType;
import org.myproject.shopping_list.models.User;
import org.myproject.shopping_list.repository.UserRepository;
import org.myproject.shopping_list.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("items")
public class ItemsController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "")
    public String currentUser(Model model){

        User currentUser = User.getCurrentUser();
        int id = currentUser.getId();
        return "redirect:/items/" + id;
    }

    @GetMapping(value="{userId}")
    public String displayAllItems(Model model, @PathVariable int userId) throws ItemNotFoundException {
        User user=itemService.getUserById(userId);
        model.addAttribute("user", user);
        List<Item> items= itemService.getAllItemsByUser(user);
        model.addAttribute("title", "All Items");
        model.addAttribute("items", items);
        return "items/index";
    }

    @GetMapping("{userId}/create")
    public String displayCreateItemForm(Model model, @PathVariable int userId ) throws ItemNotFoundException {
        User user= itemService.getUserById(userId);
        model.addAttribute("user",user);
        model.addAttribute("item", new Item());
        model.addAttribute("types", ItemType.values());
        return "items/create";
    }

    @RequestMapping(path="create", method=RequestMethod.POST)
    public String processCreateOrUpdateItemForm(@ModelAttribute @Valid Item item, Errors errors, Model model,
                                                @RequestParam(value="userId")int userId) throws ItemNotFoundException {
        if (errors.hasErrors()) {
            model.addAttribute("types", ItemType.values());
            return "items/create";
        }
        User user= itemService.getUserById(userId);
        List<Item> allItems= itemService.getAllItemsByUser(user);
        for (int i=0; i<allItems.size(); i++){
            String checkedItem=allItems.get(i).getName().toLowerCase();
            String newitem= item.getName().toLowerCase();
            if (checkedItem.equals(newitem)){
                model.addAttribute("errorMsg", "This item already exists in a list");
                model.addAttribute("types", ItemType.values());
                return "items/create";
            }
        }
        item.setUser(user);
        itemService.createItem(item);
        return "redirect:";
    }

    @GetMapping(value="{userId}/edit/{id}")
    public String displayCreateItemForm(Model model, @PathVariable("userId") int userId,
                                        @PathVariable("id") Optional<Integer> id)
                                        throws ItemNotFoundException{
        User user= itemService.getUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("types", ItemType.values());
        model.addAttribute("item", itemService.getItemById(id.get()));
        return "items/edit";
    }

    @RequestMapping(value="edit/{id}", method = RequestMethod.POST)
    public String processEditItemForm(Model model, Item item,
                                      @PathVariable("id") int id, @RequestParam("userId") int userId)
                                        throws ItemNotFoundException{
        User user= itemService.getUserById(userId);
        List<Item> userItems=itemService.getAllItemsByUser(user);
        itemService.editItem(item, id, userItems);
        return "redirect:/items";
    }

    @GetMapping(path="{userId}/add/{itemId}")
    public String displayAddItemById (Model model, @PathVariable Optional<Integer> itemId,
                                      @PathVariable(required=false) Integer groceryListIds, @PathVariable int userId)
            throws ItemNotFoundException {
        User user= itemService.getUserById(userId);
        List<GroceryList> allGroceries= itemService.getAllGroceryListsByUser(user);
        model.addAttribute("item", itemService.getItemById(itemId.get()));
        model.addAttribute("allGroceries", allGroceries);
        model.addAttribute("user", user);

        return "/items/add";
    }

    @RequestMapping(path="add/{itemId}", method=RequestMethod.POST)
    public String processAddItemById(Model model, @PathVariable Integer itemId,
                                     @RequestParam("groceryListIds") List<Integer> groceryListIds,
                                     @RequestParam("userId") int userId) throws ItemNotFoundException {
        User user=itemService.getUserById(userId);
        model.addAttribute("user", user);
            for (int i=0; i<groceryListIds.size(); i++){
                if (itemService.itemCheck(itemId,groceryListIds.get(i)) == true){
                    List<GroceryList> allGroceries= itemService.getAllGroceryListsByUser(user);
                    model.addAttribute("item", itemService.getItemById(itemId));
                    model.addAttribute("allGroceries", allGroceries);
                    model.addAttribute("errorMsg", "This item already exists in a list");
                    return "items/add";
                } else {
                    itemService.addSingleItemToGroceryList(groceryListIds.get(i), itemId);
                }
            }
        return "redirect:/items";
    }

    @RequestMapping(path="/{userId}/delete/{id}", method=RequestMethod.GET)
    public String displayDeleteItemById(Model model, @PathVariable("id") Integer id,
                                        @PathVariable("userId") int userId)
            throws ItemNotFoundException{
        User user= itemService.getUserById(userId);
        itemService.deleteItemById(id);
        return "redirect:/items";
    }
}
