package ntnu.idatt2105.WebShop.repositories;

import ntnu.idatt2105.WebShop.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface ProductRepository extends CrudRepository<Product, Long> {
    ArrayList<Product> findCalculationsByUser(User user);
}
