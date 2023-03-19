package ntnu.idatt2105.webshop.repositories;

import ntnu.idatt2105.webshop.model.Product;
import ntnu.idatt2105.webshop.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface ProductRepository extends CrudRepository<Product, Long> {
    ArrayList<Product> findProductsByUser(User user);
}
