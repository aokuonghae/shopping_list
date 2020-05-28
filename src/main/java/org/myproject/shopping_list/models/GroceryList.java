package org.myproject.shopping_list.models;


import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class GroceryList extends AbstractEntity {

    @NotBlank(message="Enter a name")
    private String name;

    @ManyToMany
    private List<Item> items;

    @ManyToOne
    private User user;

    private LocalDateTime getDate=LastBought.getLastBought();

    public GroceryList(){}

    public GroceryList(List<Item> items) {
        this.items = items;
    }

    //getters and setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    @Override
    public String toString() {
        return name;
    }


}
