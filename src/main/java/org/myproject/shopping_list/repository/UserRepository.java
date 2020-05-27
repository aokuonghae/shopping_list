package org.myproject.shopping_list.repository;

import org.myproject.shopping_list.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

<<<<<<< HEAD:src/main/java/org/myproject/shopping_list/models/data/UserRepository.java
@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Integer> {
=======
public interface UserRepository extends CrudRepository<User, Integer> {
>>>>>>> authentication:src/main/java/org/myproject/shopping_list/repository/UserRepository.java
    User findByUsername(String username);
    User findByEmail(String email);
}