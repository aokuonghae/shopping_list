package org.myproject.shopping_list.controllers;

import org.myproject.shopping_list.models.*;
import org.myproject.shopping_list.models.data.GroceryListRepository;
import org.myproject.shopping_list.models.data.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @GetMapping
    public String index(Model model) {
        model.addAttribute("title", "My Lists");
        model.addAttribute("lists", itemService.getAllGroceryLists());

        return "groceries/index";
    }

    @GetMapping("new")
    public String displayNewGroceryList(Model model){
        model.addAttribute("title", "New Grocery List");
        model.addAttribute(new GroceryList());
        model.addAttribute("items", itemService.getAllItems());
        return "groceries/new";
    }
    @PostMapping("new")
    public String processNewGroceryListForm(@ModelAttribute @Valid GroceryList newGroceryList, Errors errors,
                                            Model model, @RequestParam(value="items") List<Integer> items){
        if (errors.hasErrors()){
            model.addAttribute("title", "New Grocery List");
            return "groceries/new";
        }
        List<Item> itemList= (List<Item>)itemRepository.findAllById(items);

        newGroceryList.setItems(itemList);
        groceryListRepository.save(newGroceryList);
        return "redirect:";
    }

    @GetMapping("view/{groceryListId}")
    public String displayGroceryList(Model model, @PathVariable int groceryListId){
        Optional optGroceryList= groceryListRepository.findById(groceryListId);
        if (optGroceryList.isPresent()){
            GroceryList list= (GroceryList) optGroceryList.get();
            model.addAttribute("list", list);
            return "groceries/view";
        } else {
            return "redirect:";
        }
    }

    @GetMapping("shopping/{groceryListId}")
    public String displayMyShoppingList(Model model, @PathVariable int groceryListId,
                                        LastBought dateContainer){
        Optional optGroceryList= groceryListRepository.findById(groceryListId);

        dateContainer.setDateTime(LocalDateTime.now());
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.ENGLISH);

        String lastBought = formatter1.format(dateContainer.getDateTime());

        model.addAttribute("date", lastBought);

        if (optGroceryList.isPresent()){
            GroceryList list= (GroceryList) optGroceryList.get();
            model.addAttribute("list", list);
            return "groceries/shopping";
        } else {
            return "redirect:";
        }
    }

    @PostMapping("shopping/{groceryListId}")
    public String updateItemLastBought(Model model, @RequestParam(value="bought") List<Integer> itemIds,
                                      LastBought dateContainer) throws ItemNotFoundException {
        dateContainer.setDateTime(LocalDateTime.now());
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.ENGLISH);

        String lastBought = formatter1.format(dateContainer.getDateTime());

        if (itemIds != null) {
            for (int id : itemIds) {
               itemService.setItemTime(lastBought, id);
            }
        }
        return "redirect:/groceries/view/{groceryListId}";
    }
}
