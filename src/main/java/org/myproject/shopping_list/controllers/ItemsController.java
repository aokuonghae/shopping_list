package org.myproject.shopping_list.controllers;

import org.myproject.shopping_list.models.*;
import org.myproject.shopping_list.models.data.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("items")
public class ItemsController {
    @Autowired
    ItemService itemService;

    @RequestMapping
    public String displayAllItems(Model model){
        List<Item> items= itemService.getAllItems();
        model.addAttribute("title", "All Items");
        model.addAttribute("items", items);
        return "items/index";
    }

    @GetMapping("create")
    public String displayCreateItemForm(Model model, @RequestParam(value="message", required = false) String message){
        model.addAttribute("item", new Item());
        model.addAttribute("types", ItemType.values());
        model.addAttribute("message");
        return "items/create";
    }

    @RequestMapping(path="create", method=RequestMethod.POST)
    public String processCreateOrUpdateItemForm(Item item, Model model, RedirectAttributes redirectAttributes){
        List<Item> allItems= itemService.getAllItems();
        for (int i=0; i<allItems.size(); i++){
            String checkedItem=allItems.get(i).getName().toLowerCase();
            String newitem= item.getName().toLowerCase();
            if (checkedItem.equals(newitem)){
                redirectAttributes.addAttribute("message", "already exists.");
                redirectAttributes.addAttribute("alertClass", "alert-danger");
                model.addAttribute("types", ItemType.values());
                return "items/create";
            }
        }
        redirectAttributes.addFlashAttribute("message", "Your item has been added.");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        itemService.createItem(item);
        return "redirect:";
    }

    @GetMapping(path= {"/edit/{id}"})
    public String displayCreateItemForm(Model model, @PathVariable("id") Optional<Integer> id)
            throws ItemNotFoundException{
        model.addAttribute("types", ItemType.values());
        model.addAttribute("item", itemService.getItemById(id.get()));
        return "items/edit";
    }

    @RequestMapping(path={"/edit/{id}"}, method = RequestMethod.POST)
    public String processEditItemForm(Model model, Item item,
                                      @PathVariable("id") int id) throws ItemNotFoundException{
        itemService.editItem(item, id);
        return "redirect:/items";
    }

    @GetMapping(path="add/{itemId}")
    public String displayAddItemById (Model model, @PathVariable Optional<Integer> itemId,
                                      @PathVariable(required=false) Integer groceryListIds)
            throws ItemNotFoundException {
        List<GroceryList> allGroceries= itemService.getAllGroceryLists();
        model.addAttribute("item", itemService.getItemById(itemId.get()));
        model.addAttribute("allGroceries", allGroceries);

        return "/items/add";
    }

    @RequestMapping(path="add/{itemId}", method=RequestMethod.POST)
    public String processAddItemById(Model model, @PathVariable Integer itemId,
                                     @RequestParam("groceryListIds") List<Integer> groceryListIds) throws ItemNotFoundException {
            for (int i=0; i<groceryListIds.size(); i++){
                if (itemService.itemCheck(itemId,groceryListIds.get(i)) == true){
                    List<GroceryList> allGroceries= itemService.getAllGroceryLists();
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

    @RequestMapping(path="delete/{id}", method=RequestMethod.GET)
    public String displayDeleteItemById(Model model, @PathVariable("id") Integer id)
            throws ItemNotFoundException{
        itemService.deleteItemById(id);
        return "redirect:/items";
    }
}
