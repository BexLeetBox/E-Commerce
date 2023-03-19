package ntnu.idatt2105.WebShop.repositories;

import ntnu.idatt2105.WebShop.model.Cart;
import ntnu.idatt2105.WebShop.model.Product;
import ntnu.idatt2105.WebShop.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface CartRepository extends CrudRepository<Product, Long> {
    Cart findCartByUser(User user);
}
