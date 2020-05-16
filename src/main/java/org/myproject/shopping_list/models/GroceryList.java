package org.myproject.shopping_list.models;


import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class GroceryList extends AbstractEntity {

    @ManyToMany
    private List<Item> items;

    private String getDate=LastBought.getLastBought();

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


//    public void updateItem(int id, String lastBought) {
//        for (int i=0; i<items.size(); i++){
//            Item item=items.get(i);
//            if (item.equals(id)){
//                item.setLastBought(lastBought);
//                return;
//            }
//        }
//    }
}
