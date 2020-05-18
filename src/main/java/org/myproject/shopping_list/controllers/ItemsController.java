package org.myproject.shopping_list.controllers;

import org.myproject.shopping_list.models.Item;
import org.myproject.shopping_list.models.ItemNotFoundException;
import org.myproject.shopping_list.models.ItemService;
import org.myproject.shopping_list.models.ItemType;
import org.myproject.shopping_list.models.data.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@Controller
@RequestMapping("items")
public class ItemsController {
    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;

    @RequestMapping
    public String displayAllItems(Model model){
        model.addAttribute("title", "All Items");
        model.addAttribute("items", itemService.getAllItems());
        return "items/index";
    }

    @GetMapping("create")
    public String displayCreateItemForm(Model model){
        model.addAttribute("item", new Item());
        model.addAttribute("types", ItemType.values());
        return "items/create";
    }

    @RequestMapping(path="create", method=RequestMethod.POST)
    public String processCreateOrUpdateItemForm(Item item){
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

    @RequestMapping(path="delete/{id}", method=RequestMethod.GET)
    public String displayDeleteItemById(Model model, @PathVariable("id") Integer id)
            throws ItemNotFoundException{
        itemService.deleteItemById(id);
        return "redirect:/items";
    }
}
