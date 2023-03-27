package ntnu.idatt2105.webshop.model;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @Test
    public void testAddProduct() {
        // Create a product to test
        Product product = new Product();

        // Create a new user and add a product to it
        User user = new User();
        user.addProduct(product);

        // Ensure that the user contains the added product
        List<Product> products = user.getProducts();
        assertEquals(1, products.size());
        assertEquals(true, products.contains(product));
        assertEquals(user, product.getSeller());
    }

    @Test
    public void testRemoveProduct() {
        // Create two products to test
        Product product1 = new Product();
        Product product2 = new Product();

        // Create a new user and add two products to it
        User user = new User();
        user.addProduct(product1);
        user.addProduct(product2);

        // Remove one of the products from the user
        user.removeProduct(product1);

        // Ensure that the user contains only one product
        List<Product> products = user.getProducts();
        assertEquals(1, products.size());
        assertEquals(false, products.contains(product1));
        assertEquals(true, products.contains(product2));
        assertEquals(null, product1.getSeller());
        assertEquals(user, product2.getSeller());
    }

    @Test
    public void testGettersAndSetters() {
        // Create a new user and set its properties
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setFirstName("First");
        user.setLastName("Last");
        user.setPhoneNumber("555-555-5555");
        user.setAddress("123 Main St.");
        user.setEmail("user@example.com");
        Cart cart = new Cart(user);
        user.setCart(cart);
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        user.setProducts(products);
        Set<Role> roles = new HashSet<>();
        roles.add(new Role());
        user.setRoles(roles);

        // Ensure that the user's properties are set correctly
        assertEquals("username", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("First", user.getFirstName());
        assertEquals("Last", user.getLastName());
        assertEquals("555-555-5555", user.getPhoneNumber());
        assertEquals("123 Main St.", user.getAddress());
        assertEquals("user@example.com", user.getEmail());
        assertEquals(cart, user.getCart());
        assertEquals(products, user.getProducts());
        assertEquals(roles, user.getRoles());
    }
}
