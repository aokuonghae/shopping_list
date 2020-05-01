package org.myproject.shopping_list.models;

import jdk.jfr.Enabled;
import jdk.jfr.Event;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
public class Item extends AbstractEntity{

    @NotBlank (message="Enter an item")
    private String name;

    private ItemType itemType;

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public Item(String name){
        this.name=name;
    }

    public Item(){}

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
