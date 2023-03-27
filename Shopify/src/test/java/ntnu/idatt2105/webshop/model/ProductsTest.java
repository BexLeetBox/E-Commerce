package ntnu.idatt2105.webshop.model;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductsTest {

    @Test
    public void testAddCart() {
        // Create a user and a cart to test
        User user = new User();
        Cart cart = new Cart(user);

        // Create a new product and add a cart to it
        Product product = new Product();
        product.addCart(cart);

        // Ensure that the product contains the added cart
        Set<Cart> carts = product.getCarts();
        assertEquals(1, carts.size());
        assertEquals(true, carts.contains(cart));
    }

    @Test
    public void testRemoveCart() {
        // Create a user and two carts to test
        User user = new User();
        Cart cart1 = new Cart(user);
        Cart cart2 = new Cart(user);

        // Create a new product and add two carts to it
        Product product = new Product();
        product.addCart(cart1);
        product.addCart(cart2);

        // Remove one of the carts from the product
        product.removeCart(cart1);

        // Ensure that the product contains only one cart
        Set<Cart> carts = product.getCarts();
        assertEquals(1, carts.size());
        assertEquals(false, carts.contains(cart1));
        assertEquals(true, carts.contains(cart2));
    }

    @Test
    public void testGettersAndSetters() {
        // Create a user and a cart to test
        User user = new User();
        Cart cart = new Cart(user);

        // Create a new product and set its properties
        Product product = new Product();
        product.setBriefDescription("Brief description");
        product.setFullDescription("Full description");
        product.setCategory("Category");
        product.setLatitude(10.0);
        product.setLongitude(20.0);
        product.setPrice(100.0);
        byte[] image = {1, 2, 3};
        product.setImage(image);
        product.setSeller(user);
        Set<Cart> carts = new HashSet<>();
        carts.add(cart);
        product.setCarts(carts);

        // Ensure that the product's properties are set correctly
        assertEquals("Brief description", product.getBriefDescription());
        assertEquals("Full description", product.getFullDescription());
        assertEquals("Category", product.getCategory());
        assertEquals(10.0, product.getLatitude());
        assertEquals(20.0, product.getLongitude());
        assertEquals(100.0, product.getPrice());
        assertEquals(image, product.getImage());
        assertEquals(user, product.getSeller());
        assertEquals(carts, product.getCarts());
    }
}
