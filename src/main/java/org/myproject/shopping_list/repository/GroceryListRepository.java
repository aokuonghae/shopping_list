package org.myproject.shopping_list.repository;

import org.myproject.shopping_list.models.GroceryList;
import org.myproject.shopping_list.models.Item;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GroceryListRepository extends CrudRepository<GroceryList,Integer> {
    List<GroceryList> findByOrderByNameAsc();
}
