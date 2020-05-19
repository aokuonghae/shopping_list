package org.myproject.shopping_list.models;

import org.myproject.shopping_list.models.data.GroceryListRepository;
import org.myproject.shopping_list.models.data.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    GroceryListRepository groceryListRepository;

    public List<Item> getAllItems(){
        List<Item> result= (List<Item>) itemRepository.findAll();

        if (result.size()>0){
            return result;
        } else {
            return new ArrayList<Item>();
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

    public Item editItem(Item itemEntity, Integer id) throws ItemNotFoundException{
        Optional<Item> item = itemRepository.findById(id);
        String lastBought= itemEntity.getLastBought();
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

    public Item setItemTime(String time, Integer id) throws ItemNotFoundException{
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
        if (item.isPresent()){
            itemRepository.deleteById(id);
        }else {
            throw new ItemNotFoundException("No item exists for given id");
        }
    }

    public void deleteGroceryListById(Integer id) {
        groceryListRepository.deleteById(id);
    }

    public GroceryList deleteItemFromGroceryById(Integer groceryId, Integer id) throws ItemNotFoundException{
        Optional<Item> item = itemRepository.findById(id);
        Item actualItem= item.get();
        Optional <GroceryList> getGroceryList = groceryListRepository.findById(groceryId);
        GroceryList myList = getGroceryList.get();
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
