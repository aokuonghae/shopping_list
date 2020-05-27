package org.myproject.shopping_list.repository;

import org.myproject.shopping_list.models.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item,Integer> {
}
