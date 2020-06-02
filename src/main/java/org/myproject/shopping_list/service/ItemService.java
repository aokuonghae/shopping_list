package org.myproject.shopping_list.service;

import org.myproject.shopping_list.error.ItemNotFoundException;
import org.myproject.shopping_list.models.ConfirmationToken;
import org.myproject.shopping_list.models.GroceryList;
import org.myproject.shopping_list.models.Item;
import org.myproject.shopping_list.models.User;
import org.myproject.shopping_list.repository.ConfirmationTokenRepository;
import org.myproject.shopping_list.repository.GroceryListRepository;
import org.myproject.shopping_list.repository.ItemRepository;
import org.myproject.shopping_list.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ItemRepository itemRepository;

    @Autowired
    private GroceryListRepository groceryListRepository;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;


    public User getUserById(int userId) throws ItemNotFoundException{
        Optional<User> optUser= userRepository.findById(userId);
        if (optUser.isPresent()){
            return optUser.get();
        } else {
            throw new ItemNotFoundException("No item record exists for given id");
        }
    }

    //GET FUNCTIONS
    public List<Item> getAllItemsByUser(User user){
        List<Item> result= (List<Item>) itemRepository.findAll();
        List<Item> ownerItems= new ArrayList<>();
        for (Item item: result){
            if (item.getUser()== user){
                ownerItems.add(item);
            }
        }
        return ownerItems;
    }

    public List<GroceryList> getAllGroceryListsByUser(User user){
        List<GroceryList> result = (List<GroceryList>) groceryListRepository.findAll();
        List<GroceryList> ownerLists= new ArrayList<>();
        for (GroceryList grocery: result){
            if (grocery.getGroceryUser()==user){
                ownerLists.add(grocery);
            }
        }
        return ownerLists;
    }
    public List<ConfirmationToken> getConfirmationTokenByUserId(int userId){
        List<ConfirmationToken> tokens=(List<ConfirmationToken>)confirmationTokenRepository.findAll();
        List<ConfirmationToken> userToken = new ArrayList<>();
        for (ConfirmationToken token : tokens) {
            User user = token.getUser();
            if (user.getId() == userId) {
                userToken.add(token);
            }
        }
        return userToken;
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


    //CREATE FUNCTIONS
    public void createItem(Item itemEntity) {
        itemEntity = itemRepository.save(itemEntity);
    }

    public void createGroceryList(List<Integer> itemIds, GroceryList groceryEntity) {
        List<Item> itemList= (List<Item>) itemRepository.findAllById(itemIds);
        groceryEntity.setItems(itemList);
        groceryEntity= groceryListRepository.save(groceryEntity);
    }

    //ADD TO GROCERY FUNCTIONS
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

    public void addSingleItemToGroceryList(Integer groceryId, Integer itemId){
        Item itemToAdd= itemRepository.findById(itemId).get();
        GroceryList groceryList= groceryListRepository.findById(groceryId).get();
        List<Item> groceryItems= groceryList.getItems();
        groceryItems.add(itemToAdd);
        groceryList.setItems(groceryItems);
        GroceryList newGroceryList=groceryListRepository.save(groceryList);
    }

    //EDIT FUNCTION
    public void editItem(Item itemEntity, Integer id, List<Item> userItems) throws ItemNotFoundException{
        for (Item item : userItems) {
            if (item.getId() == id) {
                item.setItemType(itemEntity.getItemType());
                item.setName(itemEntity.getName());
                if(itemEntity.getLastBought() == null){
                    item.setStringLastBought("Not available");
                }
                item = itemRepository.save(item);
            }
        }
    }


//DELETE FUNCTIONS
    public void deleteItemById(Integer id) throws ItemNotFoundException{
        Optional <Item> item = itemRepository.findById(id);
        if (item.isPresent()){
            Item actualItem= item.get();
            List<GroceryList> result = (List<GroceryList>) groceryListRepository.findAll();
            for (GroceryList single : result) {
                List<Item> itemList = single.getItems();
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

    public void deleteItemFromGroceryById(Integer groceryId, Integer id) throws ItemNotFoundException{
        Item actualItem = itemRepository.findById(id).get();
        GroceryList myList = groceryListRepository.findById(groceryId).get();
        List<Item> previousItems= myList.getItems();
        if (previousItems.contains(actualItem)){
            previousItems.remove(actualItem);
            myList.setItems(previousItems);

            myList=groceryListRepository.save(myList);
        }else {
            throw new ItemNotFoundException("Grocery list did not include that item ID");
        }
    }

    public Boolean itemCheck(Integer itemId, Integer groceryId) throws ItemNotFoundException{
        Optional<Item> item= itemRepository.findById(itemId);
        Optional<GroceryList> list= groceryListRepository.findById(groceryId);
        if(item.isPresent() && list.isPresent()){
            Item checkedItem= item.get();
            GroceryList groceryList= list.get();
            List<Item> groceryItems= groceryList.getItems();
            return groceryItems.contains(checkedItem);
        } else {
            throw new ItemNotFoundException("Id not found");
        }

    }
    public void setItemTime(LocalDateTime time, Integer id) throws ItemNotFoundException{
        Optional<Item> item = itemRepository.findById(id);
        if (item.isPresent()){
            Item newItem=item.get();
            newItem.setLastBought(time);
            newItem= itemRepository.save(newItem);
        }else {
            throw new ItemNotFoundException("This ID does not exist");
        }
    }

}
