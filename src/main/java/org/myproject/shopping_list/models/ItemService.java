package org.myproject.shopping_list.models;

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

    public List<Item> getAllItems(){
        List<Item> result= (List<Item>) itemRepository.findAll();

        if (result.size()>0){
            return result;
        } else {
            return new ArrayList<Item>();
        }
    }

    public Item getItemById(Integer id) throws ItemNotFoundException {
        Optional<Item> item= itemRepository.findById(id);
        if(item.isPresent()) {
            return item.get();
        } else {
            throw new ItemNotFoundException("No item record exists for given id");
        }
    }

    public Item create(Item itemEntity) {
        itemEntity = itemRepository.save(itemEntity);
        return itemEntity;
    }

    public Item edit(Item itemEntity, Integer id) throws ItemNotFoundException{
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

    public void deleteItemById(Integer id) throws ItemNotFoundException{
        Optional <Item> item = itemRepository.findById(id);
        if (item.isPresent()){
            itemRepository.deleteById(id);
        }else {
            throw new ItemNotFoundException("No item exists for given id");
        }
    }
}
