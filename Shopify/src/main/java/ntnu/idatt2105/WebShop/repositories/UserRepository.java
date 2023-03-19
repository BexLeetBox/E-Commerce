package ntnu.idatt2105.webshop.repositories;

import ntnu.idatt2105.webshop.model.Product;
import ntnu.idatt2105.webshop.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
