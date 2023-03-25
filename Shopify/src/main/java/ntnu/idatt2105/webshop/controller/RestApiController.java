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

    @CrossOrigin
    @GetMapping("/account")
    @ResponseStatus(value = HttpStatus.CREATED)
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


    @CrossOrigin
    @PutMapping("/updateAccount")
    @ResponseStatus(value = HttpStatus.CREATED)
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
                if (!PasswordHashing.validatePassword(oldPassword, user.getPassword())) {
                    return ResponseEntity.badRequest().build();
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



    @ApiOperation(value = "Sell a new product", response = Map.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully sold product"),
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
        }
    }



    @CrossOrigin()
    @PostMapping(value="/addToCart")
    public ResponseEntity<Map<String, String>> addToCart(
            @RequestParam Long id,
            Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName());
        if (user == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User not found"));
        }


        Cart cart = cartRepository.findCartByUser(user);
        Product product = productRepository.findProductById(id);

        logger.info("Cart before adding product: {}", cart.getId());
        logger.info("Product to add: {}", product.getId());

        product.addCart(cart);
        cart.getProducts().add(product);
        cartRepository.save(cart);

        logger.info("Cart after adding product: {}", cart.getProducts().toString());
        return ResponseEntity.ok(Collections.singletonMap("success", "Item added to cart successfully"));
    }

    @CrossOrigin()
    @PostMapping(value="/removeFromCart")
    public ResponseEntity<Map<String, String>> removeFromCart(
            @RequestParam Long id,
            Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName());
        if (user == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User not found"));
        }


        Cart cart = cartRepository.findCartByUser(user);
        Product product = productRepository.findProductById(id);

        logger.info("Cart before adding product: {}", cart.getId());
        logger.info("Product to add: {}", product.getId());

        product.removeCart(cart);
        cart.getProducts().remove(product);
        cartRepository.save(cart);

        logger.info("Cart after adding product: {}", cart.getProducts().toString());
        return ResponseEntity.ok(Collections.singletonMap("success", "Item removed from cart successfully"));
    }



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
     * Retrieve the products in the authenticated user's cart.
     *
     * @param authentication the authentication object for the current request
     * @return a list of product DTOs
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
    }

    @CrossOrigin
    @RequestMapping(value = "/myListing", method=RequestMethod.GET)
    public ResponseEntity<List<Product>> getListing(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userRepository.findByUsername(authentication.getName());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Product> products = productRepository.findProductsBySeller(user);

        return ResponseEntity.ok(products);
    }

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