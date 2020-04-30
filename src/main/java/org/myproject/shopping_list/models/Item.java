package org.myproject.shopping_list.models;

import jdk.jfr.Enabled;
import jdk.jfr.Event;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@Entity
public class Item extends AbstractEntity{

    @NotBlank (message="Enter an item")
    private String name;

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
