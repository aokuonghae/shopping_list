package org.myproject.shopping_list.models.data;

import org.myproject.shopping_list.models.Item;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Integer> {
        List<Item> findByOrderByNameAsc();
        List<Item> findByOrderByLastBoughtDesc();

}
