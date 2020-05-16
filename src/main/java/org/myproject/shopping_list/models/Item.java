package org.myproject.shopping_list.models;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Item extends AbstractEntity{

    private ItemType itemType;

    @ManyToMany(mappedBy="items")
    private List<GroceryList> groceryLists= new ArrayList<>();

    private String lastBought;

    public Item(){}

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public List<GroceryList> getGroceryLists() {
        return groceryLists;
    }

    public String getLastBought() {
        return lastBought;
    }

    public void setLastBought(String lastBought) {
        this.lastBought = lastBought;
    }

}
