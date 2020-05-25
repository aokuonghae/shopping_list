package org.myproject.shopping_list.models;


import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class GroceryList extends AbstractEntity {

    @ManyToMany
    private List<Item> items;

    private LocalDateTime getDate=LastBought.getLastBought();

    public GroceryList(){}

    public GroceryList(List<Item> items) {
        this.items = items;
    }

    //getters and setters
    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    


}
