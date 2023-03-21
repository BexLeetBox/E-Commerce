package ntnu.idatt2105.webshop.controller;


import ntnu.idatt2105.webshop.model.Product;
import ntnu.idatt2105.webshop.model.User;
import ntnu.idatt2105.webshop.repositories.CartRepository;
import ntnu.idatt2105.webshop.repositories.ProductRepository;
import ntnu.idatt2105.webshop.repositories.UserRepository;
import ntnu.idatt2105.webshop.util.PasswordHashing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@CrossOrigin()
@RestController
public class RestApiController {

    Logger logger = LoggerFactory.getLogger(RestApiController.class);


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;

    /**
     * Creates a new user with the specified username and password and saves it to the database.
     *
     * @param username the username of the new user
     * @param password the password of the new user
     * @return a ResponseEntity containing the created user if successful, or an error response if not
     */
    @CrossOrigin
    @PostMapping("/register")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<User> createUser(@RequestParam("username") String username,
                                           @RequestParam("password") String password) {
        logger.info("Creating user: username={}, password=****", username);

        try {
            String hashedPassword = PasswordHashing.generatePasswordHash(password);
            User user = userRepository.save(new User(username, hashedPassword));
            logger.info("User created: id={}, username={}", user.getId(), user.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            logger.error("Failed to create user: username={}", username, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @CrossOrigin()
    @RequestMapping(value="/sell-item", method=RequestMethod.POST)
    public Map<String, String> process(@RequestBody Map <String, Object> req,@RequestParam("file") MultipartFile file,
                                       Authentication authentication) throws IOException {

                Map<String, String> returnObject = new HashMap<>();
                authentication.getName();
                User user = userRepository.findByUsername(authentication.getName());
                String image = Base64.getEncoder().encodeToString(file.getBytes());
                Product product = new Product((String) req.get("briefDescription"), (String) req.get("fullDescription"), (String) req.get("category"),
                        (Double) req.get("latitude"), (Double) req.get("longitude"), (Double) req.get("price"), image);
                logger.info("Sending cart of user "+user.getUsername());
                productRepository.save(product);

                return returnObject;
            }

    @CrossOrigin
    @RequestMapping(value = "/products", method=RequestMethod.GET)
    public List<Product> getProducts(Authentication authentication){
        if (authentication != null || true) {
            ArrayList<Product> products = new ArrayList<>();
            productRepository.findAll().forEach(products::add);
            return products;
        } else {
            return null;
        }
    }
}