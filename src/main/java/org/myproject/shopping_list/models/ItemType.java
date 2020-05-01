package org.myproject.shopping_list.models;

public enum ItemType {

    PRODUCE("Produce"),
    BAKERY("Bakery"),
    BREAKFAST("Breakfast"),
    MEAT("Meat"),
    DELI("Deli"),
    DAIRY("Dairy"),
    FROZEN("Frozen"),
    CANNED("Canned"),
    SNACKS("Snacks"),
    CONDIMENTS("Condiments"),
    SPICES("Spices"),
    HOUSEHOLD("Household"),
    PERSONAL("Personal"),
    DRINKS("Drinks"),
    OILS("Oils"),
    OTHER("Other");

    private String displayName;

    private ItemType(String displayName){
        this.displayName=displayName;
    }

    public String getDisplayName(){
        return displayName;
    }
}
