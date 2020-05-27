package org.myproject.shopping_list.controllers;

import org.myproject.shopping_list.repository.GroceryListRepository;
import org.myproject.shopping_list.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {
    @Autowired
    private GroceryListRepository groceryListRepository;
    @Autowired
    private ItemRepository itemRepository;

    @RequestMapping("")
    public String index(Model model) {
        return "index";
    }
}
