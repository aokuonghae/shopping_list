package org.myproject.shopping_list.controllers;

import org.myproject.shopping_list.models.GroceryList;
import org.myproject.shopping_list.models.Item;
import org.myproject.shopping_list.models.data.GroceryListRepository;
import org.myproject.shopping_list.models.data.ItemRepository;
import org.myproject.shopping_list.models.dto.GroceryListItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
