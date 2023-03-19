package ntnu.idatt2105.WebShop.repositories;

import ntnu.idatt2105.WebShop.model.Product;
import ntnu.idatt2105.WebShop.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<Product, Long> {
    User findByUsername(String username);
}
