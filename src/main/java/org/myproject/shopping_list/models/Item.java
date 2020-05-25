package org.myproject.shopping_list.models;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@Entity
public class Item extends AbstractEntity{

    private ItemType itemType;

    @ManyToMany(mappedBy="items")
    private List<GroceryList> groceryLists= new ArrayList<>();

    private LocalDateTime lastBought;

    private String stringLastBought;

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
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.ENGLISH);
        this.stringLastBought = formatter1.format(lastBought);
    }

    public String getStringLastBought() {
        return stringLastBought;
    }

    public int compareName(Item item){
        if (getName()==null || item.getName()==null ){
            return 0;
        }
        return getName().compareToIgnoreCase(item.getName());
    }

}
