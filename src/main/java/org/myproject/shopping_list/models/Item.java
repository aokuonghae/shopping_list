package org.myproject.shopping_list.models;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Item extends AbstractEntity{

    private ItemType itemType;

    @ManyToMany(mappedBy="items")
    private List<GroceryList> groceryLists= new ArrayList<>();

    private LocalDateTime lastBought;

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

    public LocalDateTime getLastBought() {
        return lastBought;
    }

    public void setLastBought(LocalDateTime lastBought) {
        this.lastBought = lastBought;
    }

}
