package org.myproject.shopping_list.controllers;

import org.myproject.shopping_list.error.ItemNotFoundException;
import org.myproject.shopping_list.models.GroceryList;
import org.myproject.shopping_list.models.Item;
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

import javax.validation.Path;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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
        List<GroceryList> groceryList= itemService.getAllGroceryLists();
        model.addAttribute("lists", groceryList);

        return "groceries/index";
    }
//
//    @GetMapping("new")
//    public String displayNewGroceryList(Model model){
//        model.addAttribute("title", "New Grocery List");
//        model.addAttribute(new GroceryList());
//        model.addAttribute("items", itemService.getAllItems());
//        return "groceries/new";
//    }
//    @PostMapping("new")
//    public String processNewGroceryListForm(@ModelAttribute @Valid GroceryList newGroceryList, Errors errors,
//                                            Model model, @RequestParam(value="items", required=false) List<Integer> items){
//        if (errors.hasErrors()){
//            model.addAttribute("title", "New Grocery List");
//            model.addAttribute("items", itemService.getAllItems());
//            return "groceries/new";
//        }
//        itemService.createGroceryList(items, newGroceryList);
//        return "redirect:";
//
//    }
//
//    @GetMapping("view/{groceryListId}")
//    public String displayGroceryList(Model model, @PathVariable int groceryListId){
//        Optional optGroceryList= groceryListRepository.findById(groceryListId);
//        if (optGroceryList.isPresent()){
//            GroceryList list= (GroceryList) optGroceryList.get();
//            model.addAttribute("list", list);
//            return "groceries/view";
//        } else {
//            return "redirect:";
//        }
//    }
//
//    @GetMapping("shopping/{groceryListId}")
//    public String displayMyShoppingList(Model model, @PathVariable int groceryListId,
//                                        LastBought day){
//        Optional optGroceryList= groceryListRepository.findById(groceryListId);
//        LocalDateTime date=day.getLastBought();
//        model.addAttribute("date", LastBought.convertLastBought(date));
//        if (optGroceryList.isPresent()){
//            GroceryList list= (GroceryList) optGroceryList.get();
//            model.addAttribute("list", list);
//            return "groceries/shopping";
//        } else {
//            return "redirect:";
//        }
//    }
//
//    @PostMapping("shopping/{groceryListId}")
//    public String updateItemLastBought(Model model, @RequestParam(value="bought") List<Integer> itemIds)
//            throws ItemNotFoundException {
//        if (itemIds != null) {
//            for (int id : itemIds) {
//               itemService.setItemTime(LocalDateTime.now(), id);
//            }
//        }
//        return "redirect:/groceries/view/{groceryListId}";
//    }
//
//    @RequestMapping(value={"/delete/{groceryListId}", "/delete/{groceryListId}/{itemId}" }, method=RequestMethod.GET)
//    public String deleteFromGroceryListById(Model model, @PathVariable(required=false) Integer itemId,
//                                            @PathVariable Integer groceryListId )
//    throws ItemNotFoundException {
//        if (itemId != null){
//            itemService.deleteItemFromGroceryById(groceryListId, itemId);
//            return "redirect:/groceries/view/{groceryListId}";
//        } else {
//            itemService.deleteGroceryListById(groceryListId);
//            return "redirect:/groceries";
//        }
//    }

}
