package org.myproject.shopping_list.models.data;

import org.myproject.shopping_list.models.GroceryList;
import org.springframework.data.repository.CrudRepository;

public interface GroceryListRepository extends CrudRepository<GroceryList,Integer> {
}
