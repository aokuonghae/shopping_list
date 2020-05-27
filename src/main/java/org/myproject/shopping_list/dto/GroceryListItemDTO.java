package org.myproject.shopping_list.dto;

import org.myproject.shopping_list.models.GroceryList;
import org.myproject.shopping_list.models.Item;

public class GroceryListItemDTO {

    private GroceryList listName;

    private Item items;

    public GroceryListItemDTO(){}

    public GroceryList getListName() {
        return listName;
    }

    public void setListName(GroceryList listName) {
        this.listName = listName;
    }

    public Item getItems() {
        return items;
    }

    public void setItems(Item items) {
        this.items = items;
    }
}
