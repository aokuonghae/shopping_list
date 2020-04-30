package org.myproject.shopping_list.controllers;

import org.myproject.shopping_list.models.Item;
import org.myproject.shopping_list.models.data.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("items")
public class ItemsController {
    @Autowired
    private ItemRepository itemRepository;

    @GetMapping
    public String displayAllItems(Model model){
        model.addAttribute("title", "All Items");
        model.addAttribute("items", itemRepository.findAll());
        return "items/index";
    }

    @GetMapping("create")
    public String displayCreateItemForm(Model model){
        model.addAttribute("title", "Create Item");
        model.addAttribute(new Item());
        return "items/create";
    }

    @PostMapping("create")
    public String processCreateItemForm(@ModelAttribute @Valid Item newItem,
                                        Errors errors, Model model){
        if (errors.hasErrors()){
            model.addAttribute("title", "Create Item");
            return "items/create";
        }
        itemRepository.save(newItem);
        return "redirect:";
    }

    @GetMapping("delete")
    public String displayDeleteItemForm(Model model){
        model.addAttribute("title", "Delete Items");
        model.addAttribute("items", itemRepository.findAll());
        return "items/delete";
    }

    @PostMapping("delete")
    public String processDeleteItemForm(@RequestParam(required=false) int[] itemIds){
        if (itemIds !=null){
            for (int id: itemIds){
                itemRepository.deleteById(id);
            }
        }
        return "redirect:";
    }
}
