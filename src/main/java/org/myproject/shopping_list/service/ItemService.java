package org.myproject.shopping_list.service;

import org.myproject.shopping_list.models.GroceryList;
import org.myproject.shopping_list.models.User;
import org.myproject.shopping_list.repository.GroceryListRepository;
import org.myproject.shopping_list.repository.ItemRepository;
import org.myproject.shopping_list.models.Item;
import org.myproject.shopping_list.error.ItemNotFoundException;

import org.myproject.shopping_list.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    GroceryListRepository groceryListRepository;

    public List<Item> getAllItems(){

        List<Item> result= (List<Item>) itemRepository.findByOrderByNameAsc();
        if (result.size()>0){
            return result;
        } else {
            return new ArrayList<Item>();
        }
    }

    public List<Item> getAllItemsByDate(){
        List<Item> result=(List<Item>) itemRepository.findByOrderByLastBoughtDesc();
        if (result.size() > 0) {
            return result;
        } else {
            return new ArrayList<Item>();
        }
    }

    public Boolean itemCheck(Integer itemId, Integer groceryId){
        Item checkedItem= itemRepository.findById(itemId).get();
        GroceryList groceryList= groceryListRepository.findById(groceryId).get();
        List<Item> groceryItems= groceryList.getItems();
        if (groceryItems.contains(checkedItem)){
            return true;
        }else {
            return false;
        }
    }

    public List<GroceryList> getAllGroceryLists(){
        List<GroceryList> result = (List<GroceryList>) groceryListRepository.findAll();

        return result;
    }


    public Item getItemById(Integer id) throws ItemNotFoundException {
        Optional<Item> item= itemRepository.findById(id);
        if(item.isPresent()) {
            return item.get();
        } else {
            throw new ItemNotFoundException("No item record exists for given id");
        }
    }

    public GroceryList getGroceryById(Integer id) throws ItemNotFoundException {
        Optional<GroceryList> groceryList = groceryListRepository.findById(id);
        if (groceryList.isPresent()){
            return groceryList.get();
        }else {
            throw new ItemNotFoundException("No Grocery record exists for given id");
        }
    }

    public Item createItem(Item itemEntity) {
        itemEntity = itemRepository.save(itemEntity);
        return itemEntity;
    }

    public GroceryList createGroceryList(List<Integer> itemIds, GroceryList groceryEntity) {
        List<Item> itemList= (List<Item>) itemRepository.findAllById(itemIds);
        groceryEntity.setItems(itemList);
        groceryEntity= groceryListRepository.save(groceryEntity);
        return groceryEntity;
    }

    public GroceryList addItemToGroceryList(GroceryList groceryEntity, Integer itemId, List<Item> itemEntity)
        throws ItemNotFoundException {

        List<Item> previousItems= groceryEntity.getItems();
        previousItems.addAll(itemEntity);

        Optional <GroceryList> groceryList = groceryListRepository.findById(groceryEntity.getId());
        if (groceryList.isPresent()) {
            GroceryList newGroceryList = groceryList.get();
            newGroceryList.setItems(previousItems);

            newGroceryList=groceryListRepository.save(newGroceryList);
            return newGroceryList;
        }else {
            throw new ItemNotFoundException("This Grocery List ID does not exist");
        }
    }

    public GroceryList addSingleItemToGroceryList(Integer groceryId, Integer itemId){
        Item itemToAdd= itemRepository.findById(itemId).get();
        GroceryList groceryList= groceryListRepository.findById(groceryId).get();
        List<Item> groceryItems= groceryList.getItems();
        groceryItems.add(itemToAdd);
        groceryList.setItems(groceryItems);
        GroceryList newGroceryList=groceryListRepository.save(groceryList);
        return newGroceryList;
    }

    public Item editItem(Item itemEntity, Integer id) throws ItemNotFoundException{
        Optional<Item> item = itemRepository.findById(id);
        LocalDateTime lastBought= itemEntity.getLastBought();
            if (item.isPresent()) {
                Item newItem = item.get();
                newItem.setItemType(itemEntity.getItemType());
                newItem.setName(itemEntity.getName());
                newItem.setLastBought(lastBought);
                newItem = itemRepository.save(newItem);
                return newItem;
            } else {
                throw new ItemNotFoundException("This ID does not exist.");
            }
    }

    public Item setItemTime(LocalDateTime time, Integer id) throws ItemNotFoundException{
        Optional<Item> item = itemRepository.findById(id);
        if (item.isPresent()){
            Item newItem=item.get();
            newItem.setLastBought(time);
            newItem= itemRepository.save(newItem);
            return newItem;
        }else {
            throw new ItemNotFoundException("This ID does not exist");
        }
    }

    public void deleteItemById(Integer id) throws ItemNotFoundException{
        Optional <Item> item = itemRepository.findById(id);
        Item actualItem= item.get();
        if (item.isPresent()){
            List<GroceryList> result = (List<GroceryList>) groceryListRepository.findAll();
            for (int i=0; i<result.size(); i++){
                GroceryList single = result.get(i);
                List<Item> itemList= single.getItems();
                if (itemList.contains(actualItem)) {
                    deleteItemFromGroceryById(single.getId(), id);
                }
            }
            itemRepository.deleteById(id);
        }else {
            throw new ItemNotFoundException("No item exists for given id");
        }
    }

    public void deleteGroceryListById(Integer id) {
        groceryListRepository.deleteById(id);
    }

    public GroceryList deleteItemFromGroceryById(Integer groceryId, Integer id) throws ItemNotFoundException{
        Item actualItem = itemRepository.findById(id).get();
        GroceryList myList = groceryListRepository.findById(groceryId).get();
        List previousItems= myList.getItems();
        if (previousItems.contains(actualItem)){
            previousItems.remove(actualItem);
            myList.setItems(previousItems);

            myList=groceryListRepository.save(myList);
            return myList;
        }else {
            throw new ItemNotFoundException("Grocery list did not include that item ID");
        }
    }

}
