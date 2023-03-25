package ntnu.idatt2105.webshop.controller;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ntnu.idatt2105.webshop.components.SessionRepository;
import ntnu.idatt2105.webshop.dto.ProductDTO;
import ntnu.idatt2105.webshop.model.Cart;
import ntnu.idatt2105.webshop.model.Message;
import ntnu.idatt2105.webshop.model.Product;
import ntnu.idatt2105.webshop.model.User;
import ntnu.idatt2105.webshop.repositories.CartRepository;
import ntnu.idatt2105.webshop.repositories.MessageRepository;
import ntnu.idatt2105.webshop.repositories.ProductRepository;
import ntnu.idatt2105.webshop.repositories.UserRepository;
import ntnu.idatt2105.webshop.util.PasswordHashing;
import org.apache.logging.log4j.LogManager;
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
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
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

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SessionRepository sessionRepository;

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
    public List<ProductDTO> getProducts(Authentication authentication){
        if (authentication != null || true) {
            Iterable<Product> products = productRepository.findAll();
            List<ProductDTO> productDTOs = new ArrayList<>();
            for (Product product : products) {
                ProductDTO productDTO = new ProductDTO(product.getId(), product.getBriefDescription(),
                        product.getFullDescription(), product.getCategory(), product.getLatitude(),
                        product.getLongitude(), product.getPrice(), product.getImage(),
                        product.getSeller().getUsername());
                productDTOs.add(productDTO);
            }
            return productDTOs;
        } else {
            return null;
        }
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
        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = new ProductDTO(product.getId(), product.getBriefDescription(),
                    product.getFullDescription(), product.getCategory(), product.getLatitude(),
                    product.getLongitude(), product.getPrice(), product.getImage(),
                    product.getSeller().getUsername());
            productDTOs.add(productDTO);
        }
        return ResponseEntity.ok(productDTOs);
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

    @CrossOrigin
    @PostMapping("/messages")
    public ResponseEntity<Message> sendMessage(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestBody String content) {

        // Look up sender and receiver by ID in the database
        User sender = userRepository.findById(senderId).orElseThrow();
        User receiver = userRepository.findById(receiverId).orElseThrow();

        // Create a new message object and save it to the database
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setCreatedAt(LocalDateTime.now());
        messageRepository.save(message);

        // Send the message over WebSocket to the recipient
        String payload = sender.getUsername() + ": " + content;
        TextMessage textMessage = new TextMessage(payload);
        try {
            WebSocketSession recipientSession = sessionRepository.getSession(receiver.getId());
            if (recipientSession != null && recipientSession.isOpen()) {
                recipientSession.sendMessage(textMessage);
            }
        } catch (IOException e) {
            logger.error("Error sending WebSocket message", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(message);
    }


}