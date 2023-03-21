package ntnu.idatt2105.webshop.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import ntnu.idatt2105.webshop.model.User;
import ntnu.idatt2105.webshop.repositories.UserRepository;
import ntnu.idatt2105.webshop.util.PasswordHashing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.stream.Collectors;

public class UserController {


    @RestController
    @EnableAutoConfiguration
    @CrossOrigin
    public class TokenController {

        public static String keyStr = "testsecrettestsecrettestsecrettestsecrettestsecret";
        @Autowired
        UserRepository userRepository;
        Logger logger = LoggerFactory.getLogger(TokenController.class);

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

        /**
         * Logs in a user with the specified username and password, if the credentials are correct.
         *
         * @param username the username of the user to log in
         * @param password the password of the user to log in
         * @return a ResponseEntity containing the logged-in user if successful, or an error response if not
         * @throws NoSuchAlgorithmException if the specified cryptographic algorithm is not available
         * @throws InvalidKeySpecException if the provided key specification is inappropriate for the given algorithm
         */
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
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }


    }
}
