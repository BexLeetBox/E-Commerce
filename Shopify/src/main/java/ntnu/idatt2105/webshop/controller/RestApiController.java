package ntnu.idatt2105.webshop.controller;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ntnu.idatt2105.webshop.dto.ProductDTO;
import ntnu.idatt2105.webshop.dto.UserDTO;
import ntnu.idatt2105.webshop.model.Cart;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents a RESTful API controller for managing users, carts, and products.
 */
@CrossOrigin()
@RestController
public class RestApiController {

    Logger logger = LoggerFactory.getLogger(RestApiController.class);
    public static String keyStr = "testsecrettestsecrettestsecrettestsecrettestsecret";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;

    /**
     * Creates a new user with the provided username, password, and email.
     *
     * @param username the username of the new user
     * @param password the password of the new user
     * @param email the email address of the new user
     * @return a response entity with the created user object and a HTTP status code of 201 if successful, or a HTTP status code of 500 if there was an error during the creation process.
     */
    @ApiOperation(value = "Create a new user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created user"),
            @ApiResponse(code = 500, message = "Failed to create user")
    })
    @CrossOrigin
    @PostMapping("/register")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<User> createUser(@RequestParam("username") String username,
                                           @RequestParam("password") String password,
                                           @RequestParam("email") String email) {
        logger.info("Creating user: username={}, password=****", username);

        try {
            String hashedPassword = PasswordHashing.generatePasswordHash(password);

            User user = userRepository.save(new User(username, hashedPassword, email));
            Cart cart = new Cart(user);
            user.setCart(cart);
            cartRepository.save(cart);
            logger.info("User created: id={}, username={}", user.getId(), user.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            logger.error("Failed to create user: username={}", username, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves the user details for the authenticated user.
     *
     * @param authentication the authentication object containing the user's credentials
     * @return a response entity with the user details and a HTTP status code of 200 if successful, or a HTTP status code of 404 if the user was not found, or a HTTP status code of 401 if the user is not authenticated.
     */
    @CrossOrigin
    @GetMapping("/account")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "Get user details", response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user details"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "User not found")
    })
    public ResponseEntity<UserDTO> getUser(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (authentication!=null && authentication.isAuthenticated()){
            UserDTO userDto = new UserDTO(user.getId(),user.getFirstName(), user.getLastName(), user.getEmail(),
                    user.getPhoneNumber(), user.getAddress(), user.getUsername());
            return ResponseEntity.ok(userDto);
        }
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }



    /**
     * Updates the user details for the authenticated user.
     *
     * @param userDto the user details to be updated
     * @param authentication the authentication object containing the user's credentials
     * @return a response entity with the updated user details and a HTTP status code of 200 if successful, or a HTTP status code of 400 if the old password is incorrect, or a HTTP status code of 401 if the user is not authenticated, or a HTTP status code of 404 if the user was not found.
     * @throws NoSuchAlgorithmException if there is an error with the password hashing algorithm
     * @throws InvalidKeySpecException if there is an error with the password hashing algorithm
     */
    @CrossOrigin
    @PutMapping("/updateAccount")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "Update user details", response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated user details"),
            @ApiResponse(code = 400, message = "Incorrect old password"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "User not found")
    })
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDto, Authentication authentication) throws NoSuchAlgorithmException, InvalidKeySpecException {
        User user = userRepository.findByUsername(authentication.getName());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (authentication!=null && authentication.isAuthenticated()){
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setAddress(userDto.getAddress());

            // Check if newPassword field is present in the request
            String newPassword = userDto.getNewPassword();
            if (newPassword != null && !newPassword.isEmpty()) {
                // Check if old password is correct
                String oldPassword = user.getPassword();
                if (!PasswordHashing.validatePassword(userDto.getOldPassword(), oldPassword)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
                // Set the new password
                user.setPassword(PasswordHashing.generatePasswordHash(newPassword));
            }

            userRepository.save(user);

            UserDTO updatedUserDto = new UserDTO(user.getId(),user.getFirstName(), user.getLastName(), user.getEmail(),
                    user.getPhoneNumber(), user.getAddress(), user.getUsername());

            return ResponseEntity.ok(updatedUserDto);
        }
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }




    /**
     * Adds a new product for sale to the marketplace.
     *
     * @param briefDescription a brief description of the product
     * @param fullDescription a detailed description of the product
     * @param category the category of the product
     * @param latitude the latitude of the location where the product is being sold
     * @param longitude the longitude of the location where the product is being sold
     * @param price the price of the product
     * @param image the image of the product
     * @param authentication the authentication object containing the user's credentials
     * @return a response entity with a success message and a HTTP status code of 200 if successful, or a HTTP status code of 400 if the user was not found or there was an error processing the image, or a HTTP status code of 500 if there was an error selling the product.
     */
    @ApiOperation(value = "Sell a new product", response = Map.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully sold product"),
            @ApiResponse(code = 400, message = "User not found or failed to process image"),
            @ApiResponse(code = 500, message = "Failed to sell product")
    })
    @CrossOrigin()
    @PostMapping(value="/sell-item", consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, String>> processSellItem(
            @RequestParam("briefDescription") String briefDescription,
            @RequestParam("fullDescription") String fullDescription,
            @RequestParam("category") String category,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam("price") Double price,
            @RequestParam("image") MultipartFile image,
            Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName());
        if (user == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User not found"));
        }

        try {
            byte[] imageBytes = image.getBytes();
            Product product = new Product(briefDescription, fullDescription, category, latitude, longitude, price,
                    imageBytes, user);
            productRepository.save(product);
            return ResponseEntity.ok(Collections.singletonMap("success", "Item listed successfully"));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Failed to process image"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Failed to sell product"));
        }
    }




    /**
     * Adds a product to the authenticated user's cart.
     *
     * @param id the ID of the product to be added to the cart
     * @param authentication the authentication object containing the user's credentials
     * @return a response entity with a success message and a HTTP status code of 200 if successful, or a HTTP status code of 400 if the user was not found or there was an error adding the product to the cart.
     */
    @CrossOrigin()
    @PostMapping(value="/addToCart")
    @ApiOperation(value = "Add product to cart", response = Map.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added item to cart"),
            @ApiResponse(code = 400, message = "User not found or failed to add item to cart")
    })
    public ResponseEntity<Map<String, String>> addToCart(
            @RequestParam Long id,
            Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName());
        if (user == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User not found"));
        }

        try {
            Cart cart = cartRepository.findCartByUser(user);
            Product product = productRepository.findProductById(id);

            logger.info("Cart before adding product: {}", cart.getId());
            logger.info("Product to add: {}", product.getId());

            product.addCart(cart);
            cart.getProducts().add(product);
            cartRepository.save(cart);

            logger.info("Cart after adding product: {}", cart.getProducts().toString());
            return ResponseEntity.ok(Collections.singletonMap("success", "Item added to cart successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Failed to add item to cart"));
        }
    }

    /**
     * Removes a product from the authenticated user's cart.
     *
     * @param id the ID of the product to be removed from the cart
     * @param authentication the authentication object containing the user's credentials
     * @return a response entity with a success message and a HTTP status code of 200 if successful, or a HTTP status code of 400 if the user was not found or there was an error removing the product from the cart.
     */
    @CrossOrigin()
    @PostMapping(value="/removeFromCart")
    @ApiOperation(value = "Remove product from cart", response = Map.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully removed item from cart"),
            @ApiResponse(code = 400, message = "User not found or failed to remove item from cart")
    })
    public ResponseEntity<Map<String, String>> removeFromCart(
            @RequestParam Long id,
            Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName());
        if (user == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User not found"));
        }

        try {
            Cart cart = cartRepository.findCartByUser(user);
            Product product = productRepository.findProductById(id);

            logger.info("Cart before removing product: {}", cart.getId());
            logger.info("Product to remove: {}", product.getId());

            product.removeCart(cart);
            cart.getProducts().remove(product);
            cartRepository.save(cart);

            logger.info("Cart after removing product: {}", cart.getProducts().toString());
            return ResponseEntity.ok(Collections.singletonMap("success", "Item removed from cart successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Failed to remove item from cart"));
        }
    }




    /**
     * Returns a list of products that match the specified categories, or all products if no categories are specified.
     *
     * @param categories the list of categories to filter products by (optional)
     * @return a list of product DTOs and a HTTP status code of 200 if successful, or a HTTP status code of 401 if the user is not authorized.
     */
    @ApiOperation(value = "Get all products", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved products"),
            @ApiResponse(code = 401, message = "Unauthorized")
    })
    @CrossOrigin
    @RequestMapping(value = "/products", method=RequestMethod.GET)
    public List<ProductDTO> getProducts(@RequestParam(required = false, name = "categories") List<String> categories){
        Iterable<Product> products;
        if (categories != null && !categories.isEmpty()) {
            // Filter products by categories
            products = productRepository.findByCategoryIn(categories);
        } else {
            // Return all products
            products = productRepository.findAll();
        }

        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = new ProductDTO(product.getId(), product.getBriefDescription(),
                    product.getFullDescription(), product.getCategory(), product.getLatitude(),
                    product.getLongitude(), product.getPrice(), product.getImage(),
                    product.getSeller().getUsername());
            productDTOS.add(productDTO);
        }
        logger.info("Product dto being sent: {}", productDTOS);
        return productDTOS;
    }



    /**
     * Returns a list of products in the authenticated user's cart.
     *
     * @param authentication the authentication object for the current request
     * @return a list of product DTOs and a HTTP status code of 200 if successful, or a HTTP status code of 401 if the user is not authorized, or a HTTP status code of 500 if there was an internal server error.
     */
    @ApiOperation(value = "Get the products in the authenticated user's cart", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Products retrieved successfully"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/myCart")
    public ResponseEntity<List<ProductDTO>> getCart(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userRepository.findByUsername(authentication.getName());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Cart cart = cartRepository.findCartByUser(user);
            Iterable<Product> products = cart.getProducts();
            List<ProductDTO> productDTOS = new ArrayList<>();
            for (Product product : products) {
                ProductDTO productDTO = new ProductDTO(product.getId(), product.getBriefDescription(),
                        product.getFullDescription(), product.getCategory(), product.getLatitude(),
                        product.getLongitude(), product.getPrice(), product.getImage(),
                        product.getSeller().getUsername());
                productDTOS.add(productDTO);
            }
            return ResponseEntity.ok(productDTOS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Returns a list of products listed by the authenticated user.
     *
     * @param authentication the authentication object for the current request
     * @return a list of product DTOs and a HTTP status code of 200 if successful, or a HTTP status code of 401 if the user is not authorized.
     */
    @CrossOrigin
    @RequestMapping(value = "/myListing", method=RequestMethod.GET)
    @ApiOperation(value = "Get the products listed by the authenticated user", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved products"),
            @ApiResponse(code = 401, message = "Unauthorized")
    })
    public ResponseEntity<List<ProductDTO>> getListing(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userRepository.findByUsername(authentication.getName());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Product> products = productRepository.findProductsBySeller(user);

        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = new ProductDTO(product.getId(), product.getBriefDescription(),
                    product.getFullDescription(), product.getCategory(), product.getLatitude(),
                    product.getLongitude(), product.getPrice(), product.getImage(),
                    product.getSeller().getUsername());
            productDTOS.add(productDTO);
        }
        logger.info("MADE IT TO RESPONSE*!!!!!");
        return ResponseEntity.ok(productDTOS);
    }


    /**
     * Removes a product from the authenticated user's listing.
     *
     * @param id the ID of the product to remove
     * @param authentication the authentication object for the current request
     * @return a success message and a HTTP status code of 200 if successful, or an error message and a HTTP status code of 400 if the user is not authorized or the product cannot be found.
     */
    @ApiOperation(value = "Remove a product from the authenticated user's listing", response = Map.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Item removed from listing successfully"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @CrossOrigin()
    @PostMapping(value="/removeFromListing")
    public ResponseEntity<Map<String, String>> removeFromListing(
            @RequestParam Long id,
            Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName());
        if (user == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User not found"));
        }

        Product product = productRepository.findProductById(id);
        if (product == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Product not found"));
        }
        // check if the product belongs to the user
        if (!product.getSeller().getUsername().equals(user.getUsername())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "You cannot remove this product"));
        }
        // remove the product from the database
        productRepository.delete(product);

        return ResponseEntity.ok(Collections.singletonMap("success", "Item removed from listing successfully"));
    }


    /**
     * Generates a token for user authentication.
     *
     * @param username the username of the user to authenticate
     * @param password the password of the user to authenticate
     * @return a token and a HTTP status code of 201 if successful, or an error message and a HTTP status code of 401 if the user is not authorized or the password is incorrect.
     * @throws Exception if an error occurs while generating the token
     */
    @ApiOperation(value = "Generate a token for user authentication", response = Map.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully generated token"),
            @ApiResponse(code = 401, message = "Unauthorized")
    })
    @CrossOrigin
    @PostMapping(value = "/token")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Map<String, String>> generateToken(@RequestParam("username") final String username, @RequestParam("password") final String password) throws Exception {

        User user = userRepository.findByUsername(username);
        if (user != null) {
            if (user.getUsername().equals(username) && PasswordHashing.validatePassword(password,user.getPassword())) {
                logger.info("/token Got user: " + username);
                String token = generateToken(username);
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Wrong Password"));
        }
        logger.info("User was not found");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Access denied, wrong credentials...."));
    }


    /**
     * Generates a JWT token for the user with the specified user ID.
     *
     * @param userId the ID of the user to generate the token for
     * @return the JWT token string
     */
    @ApiOperation(value = "Generate JWT token for user authentication", response = Map.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully generated token"),
            @ApiResponse(code = 401, message = "Unauthorized")
    })
    public String generateToken(String userId) {
        // Generate the key for signing the token
        Key key = Keys.hmacShaKeyFor(keyStr.getBytes(StandardCharsets.UTF_8));

        // Create a list of authorities for the user (in this case, a single role: "ROLE_USER")
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

        // Set the claims for the token, including the user ID and authorities
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("userId", userId);
        claims.put("authorities", grantedAuthorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        // Build and sign the token using the key
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000000)) // Set the token's expiration time
                .signWith(key)
                .compact();
    }


    /**
     * Log in a user.
     *
     * @param username the username of the user to log in
     * @param password the password of the user to log in
     * @return the user object and a HTTP status code of 201 if successful, or a HTTP status code of 500 if the login fails
     */
    @ApiOperation(value = "Log in a user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully logged in"),
            @ApiResponse(code = 500, message = "Failed to log in")
    })
    @CrossOrigin
    @PostMapping("/login")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<User> login(@RequestParam("username") String username,
                                      @RequestParam("password") String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        logger.info("User username", username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            logger.info("User with username {} not found", username);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        if (!PasswordHashing.validatePassword(password, user.getPassword())) {
            logger.info("Password for user with username {} is incorrect", username);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        logger.info("User with username {} successfully logged in", username);

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            logger.error("Error serializing response body", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}