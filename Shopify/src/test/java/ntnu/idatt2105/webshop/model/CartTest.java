package ntnu.idatt2105.webshop.model;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CartTest {

    @Test
    public void testAddProduct() {
        // Create a user and products to test
        User user = new User();
        Product product1 = new Product();
        Product product2 = new Product();

        // Create a new cart and add a product to it
        Cart cart = new Cart(user);
        cart.addProduct(product1);

        // Ensure that the cart contains the added product
        Set<Product> products = cart.getProducts();
        assertEquals(1, products.size());
        assertEquals(true, products.contains(product1));
    }

    @Test
    public void testRemoveProduct() {
        // Create a user and products to test
        User user = new User();
        Product product1 = new Product();
        Product product2 = new Product();

        // Create a new cart and add two products to it
        Cart cart = new Cart(user);
        cart.addProduct(product1);
        cart.addProduct(product2);

        // Remove one of the products from the cart
        cart.removeProduct(product1);

        // Ensure that the cart contains only one product
        Set<Product> products = cart.getProducts();
        assertEquals(1, products.size());
        assertEquals(false, products.contains(product1));
        assertEquals(true, products.contains(product2));
    }

    @Test
    public void testAddProductToCartAndProductHasCart() {
        // Create a user and a product to test
        User user = new User();
        Product product = new Product();

        // Create a new cart and add the product to it
        Cart cart = new Cart(user);
        cart.addProduct(product);

        // Ensure that the cart contains the product and the product has the cart
        Set<Product> products = cart.getProducts();
        assertEquals(1, products.size());
        assertEquals(true, products.contains(product));
        assertEquals(true, product.getCarts().contains(cart));
    }

    @Test
    public void testRemoveProductFromCartAndProductHasNoCart() {
        // Create a user and a product to test
        User user = new User();
        Product product = new Product();

        // Create a new cart and add the product to it
        Cart cart = new Cart(user);
        cart.addProduct(product);

        // Remove the product from the cart
        cart.removeProduct(product);

        // Ensure that the cart does not contain the product and the product has no cart
        Set<Product> products = cart.getProducts();
        assertEquals(0, products.size());
        assertEquals(false, products.contains(product));
        assertEquals(false, product.getCarts().contains(cart));
    }

    @Test
    public void testGettersAndSetters() {
        // Create a user and products to test
        User user = new User();
        Product product1 = new Product();
        Product product2 = new Product();

        // Create a new cart and set the user and products
        Cart cart = new Cart();
        cart.setUser(user);
        Set<Product> products = new HashSet<>();
        products.add(product1);
        products.add(product2);
        cart.setProducts(products);

        // Ensure that the cart's user and products are set correctly
        assertEquals(user, cart.getUser());
        assertEquals(products, cart.getProducts());
    }
}